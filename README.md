# Mindful

A consumer-friendly Android app blocker. Pick the apps you want to limit, set per-app and total daily time budgets, and Mindful blocks them via an overlay when the limits are reached. Freemium with a subscription tier (Phase 2).

- **Application ID:** `ivan.karpiuk.mindful` (debug: `ivan.karpiuk.mindful.debug`)
- **Min SDK:** 26 · **Target SDK:** 36 · **Compile SDK:** 36

## Tech stack

| Area | Choice |
| --- | --- |
| Language | Kotlin 2.3.20, Coroutines + Flow (no LiveData, no RxJava) |
| UI | Jetpack Compose (BOM 2026.03.01) + Material 3, no XML layouts |
| Navigation | Jetpack Navigation 3 (`androidx.navigation3`) with type-safe `NavKey`s |
| DI | Hilt 2.59.2 + KSP (no kapt) |
| Persistence | Room 2.8.2 + DataStore for prefs |
| Scheduling | WorkManager + AlarmManager |
| Blocking | AccessibilityService (foreground app detection + overlay) |
| Time | `kotlinx.datetime` (never `java.time.*`) |
| Subscriptions | RevenueCat (Phase 2) |
| Build | AGP 9.1.1, Gradle 9.3.1 |

The Gradle daemon runs on JBR 21 (pinned in `gradle/gradle-daemon-jvm.properties` and auto-provisioned via the foojay toolchain resolver) while bytecode targets JDK 17 — no manual JDK install required.

## Module layout

```
:app                    Application + MainActivity + root Nav3 graph
:core:designsystem      Material 3 theme tokens, typography
:core:domain            Pure-Kotlin usecases & entities (KMP-ready)
:core:database          Room entities & DAOs
:feature:onboarding     Permission wizard
```

`:core:domain` applies the `mindful.jvm.library` convention plugin and uses `kotlinx-coroutines-core` (not `-android`) so it stays portable. Reference modules through type-safe project accessors (e.g. `projects.core.domain`).

### Convention plugins

`build-logic/` follows the NowInAndroid pattern. Plugins exposed via the version catalog:

- `mindful.android.application`
- `mindful.android.library`
- `mindful.android.library.compose`
- `mindful.android.feature`
- `mindful.android.hilt`
- `mindful.android.room`
- `mindful.jvm.library`

The Android conventions wire JUnit 5 + MockK + Turbine + coroutines-test for unit tests and the standard test runner + ext-junit for instrumentation, so feature modules don't redeclare them.

## Architecture

Clean Architecture with hard boundaries:

- Direction: `feature/*` → `:core:domain` → `:core:database` / DataStore. Never the other way.
- `:core:domain` must not import `androidx.*`, `android.*`, or `java.time.*`.
- ViewModels expose `StateFlow<UiState>`; events are sealed-interface flows.
- Composables are stateless where possible; state hoisting is mandatory.
- `AccessibilityService` contains no business logic — it dispatches to a domain UseCase via a Hilt entry point. The state machine is pure Kotlin and unit-tested in isolation.

### Compose conventions

- Each screen has a `*Route` (DI + state) and a stateless `*Screen` (preview-friendly).
- `Modifier` is the first optional parameter, defaulting to `Modifier`.
- Use `collectAsStateWithLifecycle()`, never raw `collectAsState()`.
- Material 3 only — `androidx.compose.material:material` (Material 2) is forbidden.

## Build & run

```bash
./gradlew :app:assembleDebug              # debug build
./gradlew :app:installDebug               # install on connected device/emulator
./gradlew test                            # unit tests across all modules (JUnit 5)
./gradlew :app:testDebugUnitTest          # :app unit tests only
./gradlew check                           # full quality gate: lint + detekt + ktlint + tests
./gradlew ktlintFormat                    # auto-fix ktlint violations
```

Launch the installed debug build:

```bash
adb shell am start -n ivan.karpiuk.mindful.debug/ivan.karpiuk.mindful.MainActivity
```

## Quality gates

- **Static analysis:** detekt (`config/detekt/detekt.yml`) + ktlint (`.editorconfig`, 4-space indent, max 140), wired via `subprojects {}` reactors in the root `build.gradle.kts`. Detekt pulls in Compose-specific rules from [`io.nlopez.compose.rules:detekt`](https://mrmans0n.github.io/compose-rules/) (Material 2 ban, `ModifierMissing`, `RememberMissing`, `UnstableCollections`, `PreviewPublic`, etc.) and the `ForbiddenImport` rule blocks `androidx.compose.material.**`, `java.time.**`, `java.util.Date`, `java.text.SimpleDateFormat`.
- **Lint:** Android Lint runs as part of `./gradlew check`.

### Pre-commit hooks

[pre-commit](https://pre-commit.com/) is configured in `.pre-commit-config.yaml`. After cloning, run once:

```bash
pre-commit install
```

Hooks:

1. **pre-commit-hooks v6 file-hygiene suite** — trailing whitespace, EOF newline, YAML/TOML/XML/JSON syntax, merge-conflict markers, files larger than 500KB, private keys, mixed line endings.
2. **gitleaks** — scans staged files for secrets.
3. **forbid-main-commit** — rejects commits while `HEAD` is on `main`/`master` (override with `--no-verify` for emergency hotfixes only).
4. **gradle-static-analysis** — runs `./gradlew detekt ktlintCheck` whenever a `.kt`/`.kts` file is staged (~5s warm daemon, ~30s cold; skipped when no Kotlin files are staged).

Both `gitleaks` and `pre-commit` are available via Homebrew.

### Continuous integration

`.github/workflows/check.yml` runs the full `./gradlew check` on pushes to `main` and on PRs targeting `main`. Reports are uploaded as artifacts on failure. A separate gitleaks job enforces secret scanning at PR/push time.

## Privacy & Play policy

- The AccessibilityService shows in-app prominent disclosure before linking to system Settings.
- We **do not** request `QUERY_ALL_PACKAGES`. Package discovery uses `<queries>` with `MAIN`/`LAUNCHER` only.
- No user data leaves the device. Usage stats stay in the local Room database.
- All blocking decisions are deterministic and rule-based — no autonomous AI in the AccessibilityService.
- We never call `setUninstallBlocked` (Play policy violation), and never log third-party package names to remote servers.

## License

TBD.
