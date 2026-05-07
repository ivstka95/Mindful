# Foundation follow-ups

Tracked from the foundation hardening pass on 2026-05-04. None block day-to-day development; ordered by when they should land relative to feature work.

## P1 — Done

### ~~Wire static analysis~~ — done 2026-05-04
- detekt 1.23.7 + ktlint-gradle 14.2.0 + Android lint all wired through `subprojects {}` plugin reactors in root `build.gradle.kts`. Config at `config/detekt/detekt.yml` and `.editorconfig`. `./gradlew check` runs the full gate across every module and fails on violations. Verified by injecting a deliberate violation file (caught by both detekt and ktlint).

## P2 — Do just-in-time, when the first relevant code arrives

### Set up Hilt instrumented test infrastructure
- Production Hilt graph is wired; tests construct ViewModels directly with fakes, so no `@HiltAndroidTest` exists yet.
- Trigger: the first `@HiltAndroidTest` — most likely when the AccessibilityService Android adapter (Robolectric) or the first instrumented feature test lands.
- Action: add `androidTestImplementation(libs.hilt.android.testing)` + a `HiltTestApplication` runner (the `CustomTestRunner` pattern from NowInAndroid). Land it in the same PR as the first test that needs it — not speculatively.

### Move `DataRepository` out of `:app`
- `DataRepository` interface and `DefaultDataRepository` impl currently live in `:app/data/`. Architecturally the interface belongs in `:core:domain` and the implementation in `:core:database` (or another impl-tier module).
- Sample-only — replace, not refactor in place. When the first real repository lands (e.g. `AppLimitRepository`, `UsageStatsRepository`), delete the sample and put the real one in the right module from the start.
- No standalone task — folds into the first real-repo PR.

### Harden `org.gradle.configuration-cache.problems` to `fail`
- Currently set to `warn` to allow discovering incompatible tasks without blocking CI.
- Trigger: zero configuration-cache warnings across a full `./gradlew check :app:assembleDebug` run.
- Action: flip `org.gradle.configuration-cache.problems=warn` → `fail` in `gradle.properties` and verify CI stays green.

## P3 — Do before shipping, not before

### Validate release build end-to-end
- Only `:app:assembleDebug` / `:app:installDebug` are exercised today.
- `:app` release uses R8 (`isMinifyEnabled = true`, `isShrinkResources = true`) but `proguard-rules.pro` has no keep rules for Hilt / Room / kotlinx-serialization yet — release will likely fail at runtime.
- Trigger: before the first internal/closed-test track upload, or as soon as the AccessibilityService is wired (reflection-heavy code surfaces R8 issues fastest).
- Action: add keep rules and run `:app:assembleRelease` against a debug-signed config; iterate on rules until release builds, installs, and launches a smoke flow cleanly. Add the release smoke build to CI.

## Not a task — for context

### Placeholder modules
- `:core:database`, `:core:designsystem`, `:feature:onboarding` each contain only `Placeholder.kt` to keep Gradle happy. They will fill out as features land. Listed so it's clear the empty bodies are intentional, not abandoned scaffolding.
