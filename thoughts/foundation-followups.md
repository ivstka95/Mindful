# Foundation follow-ups

Tracked from the foundation hardening pass on 2026-05-04. None block day-to-day development; address as the project matures.

## Setup

### Set up the GitHub remote
- `git remote -v` is empty; `main` has no upstream.
- Action: `git remote add origin <url>` then `git push -u origin main`.

### Install a gitleaks pre-commit hook
- Claude Code's `scripts/forbid-secrets.sh` runs only inside the CC session; commits made elsewhere bypass it.
- Action: install `gitleaks` via `pre-commit` (or a plain `.git/hooks/pre-commit`) before the repo goes public.

### Wire static analysis
- No `lint` / `detekt` / `ktlintCheck` Gradle tasks are wired. ktlint formats only via the `format-on-edit.sh` PostToolUse hook.
- Action: add detekt + ktlint Gradle plugins (or detekt + Spotless) inside a convention plugin so CI can gate on them.

## Build hardening

### Validate release build end-to-end
- Only `:app:assembleDebug` / `:app:installDebug` are exercised today.
- `:app` release uses R8 (`isMinifyEnabled = true`, `isShrinkResources = true`) but `proguard-rules.pro` has no keep rules for Hilt / Room / kotlinx-serialization yet.
- Action: when ready to ship, add keep rules and run `:app:assembleRelease` against a debug-signed config; iterate on rules until release builds and launches cleanly.

### Set up Hilt instrumented test infrastructure
- Production Hilt graph is wired; tests construct ViewModels directly with fakes.
- Action: when adding the first `@HiltAndroidTest`, add `androidTestImplementation(libs.hilt.android.testing)` + a `HiltTestApplication` runner (the `CustomTestRunner` pattern from NowInAndroid).

## Architecture cleanup

### Move `DataRepository` out of `:app`
- `DataRepository` interface and `DefaultDataRepository` impl currently live in `:app/data/`. Architecturally the interface belongs in `:core:domain` and the implementation in `:core:database` (or another impl-tier module).
- Sample-only — replace, not refactor in place. Defer until the first real repository lands.

### Replace placeholder modules
- `:core:database`, `:core:designsystem`, `:feature:onboarding` each contain only `Placeholder.kt` to keep Gradle happy.
- They will fill out as features land. Listed here so it's clear the empty bodies are intentional, not abandoned scaffolding.
