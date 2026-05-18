package ivan.karpiuk.mindful.onboarding

import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEY_STEP = "step"

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val permissionChecker: PermissionChecker,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                OnboardingUiState(
                    step =
                        savedStateHandle
                            .get<String>(KEY_STEP)
                            ?.let(::deserializeStep)
                            ?: OnboardingStep.Welcome,
                ),
            )
        val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

        private val _navigationIntent = MutableSharedFlow<Intent>()
        val navigationIntent: SharedFlow<Intent> = _navigationIntent.asSharedFlow()

        fun onEvent(event: OnboardingEvent) {
            when (event) {
                OnboardingEvent.PrimaryActionClicked -> onPrimaryAction()
                OnboardingEvent.SecondaryActionClicked -> onSecondaryAction()
                OnboardingEvent.PermissionCheckRequested -> onPermissionCheck()
            }
        }

        private fun onPrimaryAction() {
            when (_uiState.value.step) {
                OnboardingStep.Welcome -> {
                    if (permissionChecker.isUsageAccessGranted()) {
                        advancePastUsage()
                    } else {
                        updateStep(OnboardingStep.UsageAccessWaiting)
                        emitIntent(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    }
                }
                OnboardingStep.UsageAccessWaiting -> {
                    emitIntent(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
                OnboardingStep.AccessibilityDisclosure -> {
                    updateStep(OnboardingStep.AccessibilityEnableWaiting)
                }
                OnboardingStep.AccessibilityEnableWaiting -> {
                    emitIntent(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }
                else -> Unit
            }
        }

        private fun onSecondaryAction() {
            when (_uiState.value.step) {
                OnboardingStep.AccessibilityDisclosure -> updateStep(OnboardingStep.Welcome)
                else -> Unit
            }
        }

        private fun onPermissionCheck() {
            when (_uiState.value.step) {
                OnboardingStep.UsageAccessWaiting -> {
                    if (permissionChecker.isUsageAccessGranted()) advancePastUsage()
                }
                OnboardingStep.AccessibilityEnableWaiting -> {
                    if (permissionChecker.isAccessibilityServiceEnabled()) {
                        updateStep(OnboardingStep.Complete)
                    }
                }
                else -> Unit
            }
        }

        private fun advancePastUsage() {
            val next =
                if (permissionChecker.isAccessibilityServiceEnabled()) {
                    OnboardingStep.Complete
                } else {
                    OnboardingStep.AccessibilityDisclosure
                }
            updateStep(next)
        }

        private fun updateStep(step: OnboardingStep) {
            if (_uiState.value.step == step) return
            savedStateHandle[KEY_STEP] = serializeStep(step)
            _uiState.update { it.copy(step = step) }
        }

        private fun emitIntent(intent: Intent) {
            viewModelScope.launch { _navigationIntent.emit(intent) }
        }
    }

private fun serializeStep(step: OnboardingStep): String = step::class.simpleName!!

private fun deserializeStep(name: String): OnboardingStep? =
    when (name) {
        "Welcome" -> OnboardingStep.Welcome
        "UsageAccessExplanation" -> OnboardingStep.UsageAccessExplanation
        "UsageAccessWaiting" -> OnboardingStep.UsageAccessWaiting
        "AccessibilityDisclosure" -> OnboardingStep.AccessibilityDisclosure
        "AccessibilityEnableWaiting" -> OnboardingStep.AccessibilityEnableWaiting
        "Complete" -> OnboardingStep.Complete
        else -> null
    }
