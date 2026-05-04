# Mindful — Apps Usage Time Blocker

A consumer-friendly Android app blocker. Users select apps, set per-app and total daily time limits, and the app blocks them via overlay when limits are reached. Freemium with subscription.

Bundle ID: `ivan.karpiuk.mindful`
Repository: (to be set up)

## Stack
- Kotlin 2.2.10, Coroutines + Flow only (no LiveData, no RxJava)
- Jetpack Compose + Material 3 1.4.0 (no XML layouts)
- Hilt 2.52 for DI
- Room 2.7 for persistence; DataStore for prefs
- WorkManager + AlarmManager for scheduling
- AccessibilityService for foreground app detection and overlay
- RevenueCat (Phase 2) for subscriptions
- KMP-ready: domain layer pure Kotlin, zero Android imports
- AGP 9.1, Gradle 9.1, JDK 17, target SDK 35, min SDK 26
- build-logic/ convention plugins (NowInAndroid pattern)

## Module structure (current — will grow to 14)
- `:app` — Application, MainActivity, DI graph composition root
- `:core:designsystem` — Material 3 theme tokens, custom typography
- `:core:domain` — pure Kotlin: usecases, entities (no Android deps)
- `:core:database` — Room entities and DAOs
- `:feature:onboarding` — permission wizard

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
- `./gradlew assembleDebug` — debug build
- `./gradlew testDebugUnitTest` — unit tests
- `./gradlew lint detekt ktlintCheck` — static analysis
- `./gradlew :app:installDebug` — install on connected device

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
- Pre-commit hook runs gitleaks; PostToolUse hook runs ktlintFormat.
- After 3 failed bug-fix attempts, STOP. Run `/debug` for root-cause analysis.
- Keep `/context` <= 60%. At 60%+, `/compact focus on <topic>`.

## Communication rules
- Ivan is a Senior Android Developer (8 years). Skip beginner explanations.
- When proposing architectural changes, present 2-3 alternatives with trade-offs.
- Default response language: English.
- Be terse in routine work; detailed when introducing new patterns.
