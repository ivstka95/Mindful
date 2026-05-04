# Mindful — Apps Usage Time Blocker

A consumer-friendly Android app blocker. Users select apps, set per-app and total daily time limits, and the app blocks them via overlay when limits are reached. Freemium with subscription.

Bundle ID: `ivan.karpiuk.mindful`
Repository: (to be set up)

## Stack
- Kotlin 2.3.20, Coroutines + Flow only (no LiveData, no RxJava)
- Jetpack Compose via BOM 2026.03.01 + Material 3 (no XML layouts)
- Jetpack Navigation 3 (`androidx.navigation3` 1.0.1) — type-safe `NavKey`s, requires `kotlin-serialization` plugin on any module declaring keys
- Hilt 2.59.2 + KSP for DI (no kapt)
- Room 2.8.2 for persistence; DataStore for prefs
- WorkManager + AlarmManager for scheduling
- AccessibilityService for foreground app detection and overlay
- RevenueCat (Phase 2) for subscriptions
- KMP-ready: `:core:domain` is pure Kotlin (`mindful.jvm.library`), zero Android imports
- AGP 9.1.1, Gradle 9.3.1, compileSdk 36, targetSdk 35, minSdk 26
- Toolchains: bytecode target is JDK 17 (Kotlin `JvmTarget.JVM_17` + Java `sourceCompatibility/targetCompatibility = VERSION_17`); the Gradle daemon itself runs on JBR 21, pinned in `gradle/gradle-daemon-jvm.properties` and auto-provisioned via the `org.gradle.toolchains.foojay-resolver-convention` plugin in `settings.gradle.kts` — no manual JDK install required.
- `build-logic/` convention plugins (NowInAndroid pattern), exposed via the version catalog: `mindful.android.application`, `mindful.android.library`, `mindful.android.library.compose`, `mindful.android.feature`, `mindful.android.hilt`, `mindful.android.room`, `mindful.jvm.library`. The Android conventions auto-add `testImplementation` (JUnit 5 + MockK + Turbine + coroutines-test) and `androidTestImplementation` (test runner + ext-junit), and configure `useJUnitPlatform()` for unit-test tasks — modules don't redeclare these.

## Module structure (5 modules today — will grow as features land)
- `:app` — `MindfulApplication` (`@HiltAndroidApp`), `MainActivity` (`@AndroidEntryPoint`), root `MainNavigation` (Nav3). DI graph composition root. End-to-end Hilt is wired: `MainScreenViewModel` is `@HiltViewModel`-injected, `DataRepository` is `@Binds`-bound in `data/DataModule.kt`. Sources under `ivan/karpiuk/mindful/`.
- `:core:designsystem` — Material 3 theme tokens, custom typography
- `:core:domain` — pure Kotlin (`mindful.jvm.library`): usecases, entities. Uses `kotlinx-coroutines-core` (not `-android`) to keep KMP-portable.
- `:core:database` — Room entities and DAOs. Applies `mindful.android.room` (Room runtime + KSP compiler + schema export) and `mindful.android.hilt`.
- `:feature:onboarding` — permission wizard

Settings file enables `TYPESAFE_PROJECT_ACCESSORS`, so reference modules as `projects.core.domain` etc., not string paths.

## Architecture rules (Clean Architecture, hard boundaries)
- Direction: feature/* -> :core:domain -> :core:database/datastore. Never the other way.
- :core:domain MUST NOT import androidx, android, java.time.*. Use kotlinx.datetime.
- ViewModels expose StateFlow<UiState> only; events are sealed interface flows.
- Composables are stateless where possible; state hoisting is mandatory.
- No business logic in AccessibilityService — dispatch to a domain UseCase via Hilt entry point.

## Compose rules
- Prefer remember + derivedStateOf over computing in composition.
- Material 3 only; do not import Material 2 (`androidx.compose.material:material`).
- Each screen has *Route (DI + state) and *Screen (stateless, preview-friendly).
- Always pass `Modifier` as first optional parameter; default to `Modifier`.
- Use `collectAsStateWithLifecycle()`, not raw `collectAsState()`.

## Testing rules
- Unit tests: JUnit5 + MockK + Turbine.
- TDD is mandatory for domain UseCases and ViewModels: red -> green -> refactor.
- AccessibilityService logic split: pure Kotlin StateMachine (JUnit5) + Android adapter (Robolectric).
- Coverage gate: domain >= 90%, feature >= 70%, service >= 60%.

## Build commands
- `./gradlew :app:assembleDebug` — debug build
- `./gradlew :app:installDebug` — install on connected device/emulator (debug applicationId is `ivan.karpiuk.mindful.debug`)
- `./gradlew test` — unit tests across all modules (JUnit 5 via `useJUnitPlatform()` configured in convention plugins)
- `./gradlew :app:testDebugUnitTest` — `:app` unit tests only
- Launch: `adb shell am start -n ivan.karpiuk.mindful.debug/ivan.karpiuk.mindful.MainActivity`
- Static analysis (lint/detekt/ktlint) is **not yet wired**. ktlint runs only via the `scripts/format-on-edit.sh` PostToolUse hook.

## Knowledge sources (in priority order)
- For Android platform: ALWAYS use `android docs search "<query>"` before web search.
- For libraries (Compose, Hilt, kotlinx.datetime): use Context7 MCP.
- For AccessibilityService policy: use `android docs search` + DeepWiki MCP (study existing apps).

## Privacy & Play policy invariants
- AccessibilityService MUST show in-app prominent disclosure before linking to Settings.
- We DO NOT request QUERY_ALL_PACKAGES. We use <queries> with MAIN/LAUNCHER only.
- We collect no user data off-device. Usage stats stay in local Room DB.
- All blocking decisions deterministic and rule-based. No autonomous AI in AccessibilityService.

## Hard NOs
- Never bypass Play's AccessibilityService restrictions.
- Never call BillingClient directly; route through RevenueCat Purchases (Phase 2).
- Never write to MediaStore or external storage.
- Never log package names of third-party apps to remote servers.
- Never request QUERY_ALL_PACKAGES.
- Never use `setUninstallBlocked` — Play policy violation.
- Never store passwords/PINs in plaintext.
- Never import `java.time.*`. Always `kotlinx.datetime`.
- Never import `androidx.compose.material:material` (Material 2). Always Material 3.

## Workflow rules
- Every feature: `/brainstorm` -> `thoughts/specs/<feature>.md` -> `/write-plan` -> `/execute-plan`.
- Long features use git worktrees (Superpowers `using-git-worktrees`).
- Claude Code hooks (configured in `.claude/settings.json`):
  - PostToolUse on Edit/Write → `scripts/format-on-edit.sh` (ktlint format)
  - PreToolUse on Bash → `scripts/forbid-secrets.sh` (secret guard within the CLI session)
- Git pre-commit hook: gitleaks via [pre-commit](https://pre-commit.com/) (`.pre-commit-config.yaml`). Contributors run `pre-commit install` once after cloning; commits are scanned for leaked secrets before they land. Both `gitleaks` and `pre-commit` are available via Homebrew.
- After 3 failed bug-fix attempts, STOP. Run `/debug` for root-cause analysis.
- Keep `/context` <= 60%. At 60%+, `/compact focus on <topic>`.
- Open follow-ups for the foundation are tracked in `thoughts/foundation-followups.md`.

## Communication rules
- Ivan is a Senior Android Developer (8 years). Skip beginner explanations.
- When proposing architectural changes, present 2-3 alternatives with trade-offs.
- Default response language: English.
- Be terse in routine work; detailed when introducing new patterns.
