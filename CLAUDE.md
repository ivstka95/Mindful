# Mindful — Apps Usage Time Blocker
A consumer-friendly Android app blocker. Users select apps, set per-app and total daily time limits, and the app blocks them via overlay when limits are reached. Freemium with subscription.
Bundle ID: `ivan.karpiuk.mindful`
Repository: https://github.com/ivstka95/Mindful
## Stack
- Kotlin 2.3.21, Coroutines + Flow only (no LiveData, no RxJava)
- Jetpack Compose via BOM 2026.05.00 + Material 3 (no XML layouts)
- Jetpack Navigation 3 (`androidx.navigation3` 1.1.1) — type-safe `NavKey`s, requires `kotlin-serialization` plugin on any module declaring keys
- Hilt 2.59.2 + KSP for DI (no kapt)
- Room 2.8.4 for persistence; DataStore for prefs
- WorkManager + AlarmManager for scheduling
- AccessibilityService for foreground app detection and overlay
- RevenueCat (Phase 2) for subscriptions
- KMP-ready: `:core:domain` is pure Kotlin (`mindful.jvm.library`), zero Android imports
- AGP 9.2.1, Gradle 9.5.0, compileSdk 36, targetSdk 36, minSdk 26
- Bytecode target: JDK 17 (`JvmTarget.JVM_17` + `VERSION_17`). Gradle daemon runs on JBR 21, pinned in `gradle/gradle-daemon-jvm.properties`, auto-provisioned via `foojay-resolver-convention` — no manual JDK install.
- `build-logic/` convention plugins (NowInAndroid pattern), exposed via the version catalog: `mindful.android.application`, `mindful.android.library`, `mindful.android.library.compose`, `mindful.android.feature`, `mindful.android.hilt`, `mindful.android.room`, `mindful.jvm.library`. The Android conventions auto-add `testImplementation` (JUnit 5 + MockK + Turbine + coroutines-test) and `androidTestImplementation` (test runner + ext-junit), and configure `useJUnitPlatform()` — modules don't redeclare these.
## Module structure
5 modules today — grows as features land.
- `:app` — `MindfulApplication` (`@HiltAndroidApp`), `MainActivity` (`@AndroidEntryPoint`), root `MainNavigation` (Nav3). DI graph composition root. End-to-end Hilt is wired: `MainScreenViewModel` is `@HiltViewModel`- injected, `DataRepository` is `@Binds`-bound in `data/DataModule.kt`. Sources under `ivan/karpiuk/mindful/`.
- `:core:designsystem` — Material 3 theme tokens, custom typography
- `:core:domain` — pure Kotlin (`mindful.jvm.library`): usecases, entities. Uses `kotlinx-coroutines-core` (not `-android`) to stay KMP-portable.
- `:core:database` — Room entities and DAOs. Applies `mindful.android.room` and `mindful.android.hilt`.
- `:feature:onboarding` — permission wizard
Settings file enables `TYPESAFE_PROJECT_ACCESSORS` — reference modules as `projects.core.domain`, not string paths.
## Architecture rules
These extend the global Clean Architecture principles for this project specifically:
- `:core:domain` MUST have zero Android imports. The `mindful.jvm.library` convention enforces this at the compiler level.
- No business logic in `AccessibilityService` — dispatch to a domain UseCase via Hilt `@EntryPoint`.
- Room entities live only in `:core:database`. Domain entities in `:core:domain` are separate types — mappers bridge them.
## Build commands
```bash
./gradlew :app:assembleDebug                # debug build
./gradlew :app:installDebug                 # install (debug ID: ivan.karpiuk.mindful.debug)
./gradlew test                              # unit tests across all modules
./gradlew :app:testDebugUnitTest            # :app unit tests only
./gradlew check                             # full quality gate (lint + detekt + ktlint + tests)
./gradlew ktlintFormat                      # auto-fix ktlint violations
adb shell am start -n ivan.karpiuk.mindful.debug/ivan.karpiuk.mindful.MainActivity
```
## Privacy & Play policy invariants (non-negotiable)
- AccessibilityService MUST show in-app prominent disclosure before linking to Settings.
- We DO NOT request `QUERY_ALL_PACKAGES`. We use `<queries>` with `MAIN`/`LAUNCHER` only.
- We collect no user data off-device. Usage stats stay in local Room DB.
- All blocking decisions are deterministic and rule-based. No autonomous AI in AccessibilityService.
- Policy note: AccessibilityService policy has tightened repeatedly since 2024.
- Always verify current requirements via Developer Knowledge MCP before submitting to Play, not from training memory.
## Hard NOs
- Never bypass Play's AccessibilityService restrictions.
- Never call BillingClient directly — route through RevenueCat (Phase 2).
- Never write to MediaStore or external storage.
- Never log package names of third-party apps to remote servers.
- Never request `QUERY_ALL_PACKAGES`.
- Never use `setUninstallBlocked` — Play policy violation.
- Never store passwords/PINs in plaintext.
## Workflow rules
### Branching
Branching discipline is in the global CLAUDE.md. Enforcement on this project:
- **Local**: `forbid-main-commit` pre-commit hook + `Bash(git push * main*)` deny in `.claude/settings.json`.
- **Server-side**: GitHub branch protection on `main` (all 3 CI jobs must pass, enforce_admins=true, no force pushes).
## Task execution flow
Follows the global CLAUDE.md flow. Project-specific conventions:
- Specs saved to `thoughts/specs/<task>.md`
- Open follow-ups tracked in `thoughts/foundation-followups.md`
### Claude Code hooks (`.claude/settings.json`)
- PostToolUse on Edit/Write → `scripts/format-on-edit.sh` (ktlint format)
- PreToolUse on Bash → `scripts/forbid-secrets.sh` (secret guard)
### Pre-commit hooks (`.pre-commit-config.yaml`)
Contributors run `pre-commit install` once after cloning. Hooks:
1. `pre-commit-hooks` v6 — file hygiene (whitespace, EOF, YAML/TOML/XML/JSON syntax, merge conflicts, large files >500KB, private keys, line endings)
2. `gitleaks` — scans staged files for secrets
3. `forbid-main-commit` — rejects commits on `main`/`master`
4. `gradle-static-analysis` — runs `./gradlew detekt ktlintCheck` when `.kt`/`.kts` files are staged (~5s warm, ~30s cold)
## CI (`.github/workflows/check.yml`)
Triggered on push to `main`, PRs targeting `main`, and manual dispatch.
Three parallel jobs — all must pass before merge:

| Job ID | Timeout | What it runs |
|---|---|---|
| `pre-commit` | 10 min | All `.pre-commit-config.yaml` hooks: gitleaks (full history), actionlint, file hygiene, gradle-static-analysis |
| `gradle` | 30 min | `./gradlew check` + `./gradlew :app:assembleDebug`. Submits dependency graph. Uploads reports on failure; APK on PR success. |
| `dependency-review` | — | PRs only, `needs: gradle`. Fails on high-severity CVEs or disallowed licenses. |
Additional workflows:
- `keep-prs-current.yml` — auto-updates open PRs when main moves.
- `release-please.yml` — opens Release PRs, bumps `VERSION_NAME` in `gradle.properties`, populates `CHANGELOG.md`. Requires `RELEASE_PLEASE_TOKEN` secret. After merging, manually increment `VERSION_CODE`.
Diagnosing CI failures: `gh run view --log-failed`
Reproduce locally: `pre-commit run --all-files` or `./gradlew check :app:assembleDebug`
Useful: `gh run list`, `gh run watch`, `gh pr checks --watch`
## Open items
Follow-ups tracked in `thoughts/foundation-followups.md`.
