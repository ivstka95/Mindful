# Changelog

## [0.2.0](https://github.com/ivstka95/Mindful/compare/v0.1.2...v0.2.0) (2026-05-19)


### ✨ Features

* **data:** implement data layer — Room database and DataStore ([#53](https://github.com/ivstka95/Mindful/issues/53)) ([4eca892](https://github.com/ivstka95/Mindful/commit/4eca89278d6167ce62be74f93cc00899d5186cbe))
* **domain:** implement core domain layer ([#52](https://github.com/ivstka95/Mindful/issues/52)) ([d1716da](https://github.com/ivstka95/Mindful/commit/d1716daa96ef67e39180c5d9e23c06366f552573))
* **onboarding:** implement permission onboarding flow ([#64](https://github.com/ivstka95/Mindful/issues/64)) ([a84e424](https://github.com/ivstka95/Mindful/commit/a84e4248cb89d91ac9a8676e3e0813cf7f26868e))


### 🐛 Bug Fixes

* **ci:** add --comment flag to code-review prompt ([#58](https://github.com/ivstka95/Mindful/issues/58)) ([7eae809](https://github.com/ivstka95/Mindful/commit/7eae8096009bdf988ae24a366ccc75942cd59543))
* **ci:** add additional_permissions and show_full_output to claude-code-review ([#55](https://github.com/ivstka95/Mindful/issues/55)) ([94d552d](https://github.com/ivstka95/Mindful/commit/94d552dbfbbaaa73752fdf1d8f6f970b8ea37516))
* **ci:** allow dependabot bot in claude-review via allowed_bots ([#49](https://github.com/ivstka95/Mindful/issues/49)) ([201a84d](https://github.com/ivstka95/Mindful/commit/201a84d59dd8e1527926151510ff093ec000b00f))
* **ci:** allow Unicode-3.0 and CDDL-1.0 in dependency-review ([#56](https://github.com/ivstka95/Mindful/issues/56)) ([77e475e](https://github.com/ivstka95/Mindful/commit/77e475ed1943855342c308d554e996d5e8eacb53))
* **ci:** grant pull-requests write permission to claude-code-review ([#54](https://github.com/ivstka95/Mindful/issues/54)) ([51c9975](https://github.com/ivstka95/Mindful/commit/51c9975bf68dcbb33a9a30e6c34be530ecd78978))
* **ci:** skip claude-review for Dependabot PRs ([#51](https://github.com/ivstka95/Mindful/issues/51)) ([ef70c1a](https://github.com/ivstka95/Mindful/commit/ef70c1ae38c4a62ece32353c291437b73583a62e))


### 🔧 Chores

* **ci:** Bump actions/dependency-review-action from 4.9.0 to 5.0.0 ([#47](https://github.com/ivstka95/Mindful/issues/47)) ([7d8f2db](https://github.com/ivstka95/Mindful/commit/7d8f2db0849f44a575e77c41a6383bdc97473890))
* **ci:** Bump anthropics/claude-code-action in the actions-all group ([#59](https://github.com/ivstka95/Mindful/issues/59)) ([b304ec9](https://github.com/ivstka95/Mindful/commit/b304ec96bbd279d96c35d7dec973c6b5b55dc332))
* **deps:** Bump androidx.datastore:datastore-preferences ([#60](https://github.com/ivstka95/Mindful/issues/60)) ([d27d720](https://github.com/ivstka95/Mindful/commit/d27d7202c07a815787ed2c45844ca251f5bcea08))
* **deps:** Bump gradle-wrapper from 9.5.0 to 9.5.1 ([#63](https://github.com/ivstka95/Mindful/issues/63)) ([9cc6764](https://github.com/ivstka95/Mindful/commit/9cc6764852ebbadaffe762e6bf8e67cdfb5a4221))
* **deps:** Bump ksp from 2.3.7 to 2.3.8 ([#61](https://github.com/ivstka95/Mindful/issues/61)) ([2368285](https://github.com/ivstka95/Mindful/commit/2368285d9fe7cbe2cd1feae37ef3f890370d4b67))
* **deps:** Bump org.robolectric:robolectric from 4.15.1 to 4.16.1 ([#62](https://github.com/ivstka95/Mindful/issues/62)) ([e0ab89a](https://github.com/ivstka95/Mindful/commit/e0ab89a93f6ff243e416c21394b8e9dc38d2f507))
* **deps:** Bump the kotlin group across 1 directory with 4 updates ([#48](https://github.com/ivstka95/Mindful/issues/48)) ([05b66ac](https://github.com/ivstka95/Mindful/commit/05b66ac98d864fde7b34e6818a9ed73775d8f9aa))

## [0.1.2](https://github.com/ivstka95/Mindful/compare/v0.1.1...v0.1.2) (2026-05-07)


### 🐛 Bug Fixes

* **ci:** scope keep-prs-current permissions to job level ([#42](https://github.com/ivstka95/Mindful/issues/42)) ([baa3ec0](https://github.com/ivstka95/Mindful/commit/baa3ec0842dd445920733dea68acc28099b7f2f1))


### 🔧 Chores

* enable Gradle parallel, configuration cache, and incremental Kotlin compilation ([#40](https://github.com/ivstka95/Mindful/issues/40)) ([ef7dfa6](https://github.com/ivstka95/Mindful/commit/ef7dfa67849e38198ac95b266c3fa53bdc294234))

## [0.1.1](https://github.com/ivstka95/Mindful/compare/v0.1.0...v0.1.1) (2026-05-07)


### 🐛 Bug Fixes

* **hooks:** make forbid-secrets.sh actually block on Claude Code ([5fed0aa](https://github.com/ivstka95/Mindful/commit/5fed0aad228347c2b094a7e8aa9d37a246c1f70f))


### 🔧 Chores

* add Dependabot and auto-merge for patch updates ([#7](https://github.com/ivstka95/Mindful/issues/7)) ([383a6d4](https://github.com/ivstka95/Mindful/commit/383a6d4c76f8dc90512a2def2536a4268797f47f))
* add gitleaks pre-commit hook ([faa76f9](https://github.com/ivstka95/Mindful/commit/faa76f94ec676de441174c96036cd91faafb61af))
* **ci:** Bump dependabot/fetch-metadata from 2 to 3 ([#9](https://github.com/ivstka95/Mindful/issues/9)) ([521a5a1](https://github.com/ivstka95/Mindful/commit/521a5a1808840960aba342bd4b347dacc6d788d6))
* cleanup .gitignore, sync CLAUDE.md, log foundation follow-ups ([33cc22a](https://github.com/ivstka95/Mindful/commit/33cc22a4bb8f456fe8e82ccba32ac93b057d0d97))
* **deps:** Bump androidx.compose:compose-bom in the compose group ([#26](https://github.com/ivstka95/Mindful/issues/26)) ([31063a6](https://github.com/ivstka95/Mindful/commit/31063a6c754ba76ec9800e1c597384e4dab5bf5c))
* **deps:** Bump androidx.compose:compose-bom in the compose group ([#8](https://github.com/ivstka95/Mindful/issues/8)) ([2ac3bd2](https://github.com/ivstka95/Mindful/commit/2ac3bd2dd78736e7166685f8af52dc4ae30a00a1))
* **deps:** Bump app.cash.turbine:turbine from 1.2.0 to 1.2.1 ([#19](https://github.com/ivstka95/Mindful/issues/19)) ([82b1a38](https://github.com/ivstka95/Mindful/commit/82b1a380f93d7678dff1324cfd97dce0c47aca74))
* **deps:** Bump com.android.tools.build:gradle ([#27](https://github.com/ivstka95/Mindful/issues/27)) ([5de0d33](https://github.com/ivstka95/Mindful/commit/5de0d333f3be1c48642eca9818a027ad4f80eb49))
* **deps:** Bump gradle-wrapper from 9.3.1 to 9.5.0 ([#18](https://github.com/ivstka95/Mindful/issues/18)) ([010226e](https://github.com/ivstka95/Mindful/commit/010226ea6d06d74c2a3630311a13572b3a8b03a5))
* **deps:** Bump io.mockk:mockk from 1.13.13 to 1.14.9 ([#16](https://github.com/ivstka95/Mindful/issues/16)) ([a6ac257](https://github.com/ivstka95/Mindful/commit/a6ac25786aaa295b75cddabe0fc6fd7636426c4d))
* **deps:** Bump org.junit.jupiter:junit-jupiter from 5.11.4 to 5.14.4 ([#17](https://github.com/ivstka95/Mindful/issues/17)) ([5205ad8](https://github.com/ivstka95/Mindful/commit/5205ad8dcf2beefcb92f21242a35a5a2c4f2fd77))
* **deps:** Bump org.junit.platform:junit-platform-launcher ([#14](https://github.com/ivstka95/Mindful/issues/14)) ([8bd8c22](https://github.com/ivstka95/Mindful/commit/8bd8c22d5e86b9c185291af9c063ffdfe61805bf))
* **deps:** Bump the androidx-misc group with 3 updates ([#13](https://github.com/ivstka95/Mindful/issues/13)) ([622aab7](https://github.com/ivstka95/Mindful/commit/622aab708a4446562edf204782968c7598b00abd))
* **deps:** Bump the kotlin group with 7 updates ([#11](https://github.com/ivstka95/Mindful/issues/11)) ([ff048f1](https://github.com/ivstka95/Mindful/commit/ff048f1e4c1b0ed4bee723487307619d76a615f9))
* **deps:** Bump the room group across 1 directory with 5 updates ([#10](https://github.com/ivstka95/Mindful/issues/10)) ([0d79d6c](https://github.com/ivstka95/Mindful/commit/0d79d6c4e9f5ce1627a4baaae884a5f4e038f3a0))
* expand Claude Code permission allowlist for greater autonomy ([#36](https://github.com/ivstka95/Mindful/issues/36)) ([bab5f33](https://github.com/ivstka95/Mindful/commit/bab5f3314b0b2ca2e1623a6542a5b1bb6b50cc42))
* harden CI, add CODEOWNERS and templates ([#5](https://github.com/ivstka95/Mindful/issues/5)) ([bf26dc1](https://github.com/ivstka95/Mindful/commit/bf26dc1e1c182e5106438417e7f90b4bea76a809))
* harden local quality gates ([#4](https://github.com/ivstka95/Mindful/issues/4)) ([f8258e8](https://github.com/ivstka95/Mindful/commit/f8258e88b15c11d960949f566db7b0659b29784a))
* replace dependabot auto-merge with conflict rebase workflow ([#20](https://github.com/ivstka95/Mindful/issues/20)) ([0cd2a3c](https://github.com/ivstka95/Mindful/commit/0cd2a3c5b080bd036b91d704963754489becddba))
* satisfy initial detekt/lint findings + apply ktlint format sweep ([0dc55c5](https://github.com/ivstka95/Mindful/commit/0dc55c541a2ae87e7489d6d4e2b2ec243abda541))

## Changelog

All notable changes to Mindful will be documented in this file.
