package ivan.karpiuk.mindful.domain.usecase

import ivan.karpiuk.mindful.domain.model.BlockingCommand
import ivan.karpiuk.mindful.domain.model.BlockingDecision

class BlockingStateMachine(
    private val evaluateBlocking: EvaluateBlockingDecisionUseCase,
) {
    private var currentForegroundApp: String? = null

    suspend fun onAppForegrounded(packageName: String): BlockingCommand {
        currentForegroundApp = packageName
        return when (val decision = evaluateBlocking(packageName)) {
            is BlockingDecision.Allow -> BlockingCommand.DoNothing
            is BlockingDecision.Block -> BlockingCommand.ShowOverlay(packageName, decision.reason)
        }
    }

    fun onAppBackgrounded(): BlockingCommand {
        currentForegroundApp = null
        return BlockingCommand.DismissOverlay
    }

    fun getCurrentForegroundApp(): String? = currentForegroundApp
}
