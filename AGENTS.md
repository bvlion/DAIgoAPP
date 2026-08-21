# Repository Guidelines

## プロジェクト概要

DAIgoAPPはAndroidスマートフォン専用アプリで、application moduleは `app/` のみです。

- Jetpack Compose
- Retrofit / OkHttp / kotlinx.serialization
- Room / DataStore
- Java 17、リポジトリ同梱のGradle Wrapper

backendは [bvlion/DAIgoAPI2](https://github.com/bvlion/DAIgoAPI2) です。

## 変更方針

- Issueで依頼された範囲の解決に必要な最小限の差分にしてください。
- 無関係なリファクタリング、命名変更、一括整形、dependency updateを混ぜないでください。
- 挙動を推測だけで変更せず、既存の実装・呼び出し元・テスト・GitHub Actions workflowを確認してください。
- DAIgoAPPの規模に対して不要なlayer / abstractionを追加しないでください。
- Android専用構成のため、明示的な要件がない限りKMP / iOS構成を再導入しないでください。
- AdMob / Google Mobile Ads SDK、Firebase App Distribution、Firebase Analytics / Firebase Crashlyticsは撤去済みです。「以前あったから」という理由で再導入しないでください。

## コーディング / 依存管理

DAIgoAPPには現時点で `.editorconfig` が存在しません。存在しないファイルへの準拠を前提にせず、Kotlin / Gradle Kotlin DSLの既存styleを確認して維持してください（現行コードは2-space indentation）。変更対象外のコードを機械的に一括整形しないでください。

dependency versionは原則として `gradle/libs.versions.toml` で管理します。HTTP通信は既存のRetrofit / OkHttp / kotlinx.serialization構成を優先してください。

## ビルドとテスト

代表的なコマンド:

- `./gradlew :app:assembleDebug`
- `./gradlew :app:testDebugUnitTest`
- `./gradlew :app:connectedCheck`（端末またはエミュレーターが必要）
- `git diff --check`

すべての変更で全コマンドの実行を必須とはしません。変更範囲に対応する最小限かつ十分な検証を行ってください。

検証を実行できなかった場合、または失敗した場合は成功として扱わず、実行したコマンドと未実施・失敗の理由をPull Request本文に記載してください。

DAIgoAPPはスマートフォン専用です。ローカル環境にWear OS emulator等の対象外端末が同時接続されており、その端末だけでinstrumentation testが失敗した場合は、対象phone端末やCI環境との差異を確認してください。その失敗を回避するためにアプリ仕様を変更しないでください。

## 秘密情報とローカル設定

- 新しい秘密値・API token・password・service account JSONをコミットしないでください。
- `google-play-service.json` / `.envrc` は既存の`.gitignore`の扱いを維持してください。
- 必要な秘密情報が環境にない場合、ダミー値のコミットや設定の迂回でbuildを通さず、未検証として報告してください。
- release signingの検証に必要な環境がない場合は未検証として報告してください。
- `release.keystore` は既にリポジトリで管理されている署名ファイルです。明示的な依頼がない限り削除・置換・ローテーションしないでください。

## Git / Pull Request

- `main`へ直接commit / pushしないでください。
- force push等の破壊的操作を行わないでください。
- Issue対応では専用branchを使用してください。branch名は現在使われている `feature/issue-<issue番号>-<short-task>` 系を基本とします。
- 調査 → 実装 → 検証 → commit → push → Ready for reviewのPR作成、まで行ってください。
- PR本文には `Closes #<issue>`、目的、変更内容、最終的な検証結果、未検証事項を記載してください。
- 解消済みの試行錯誤を大量にPR本文へ残さないでください。
- ユーザーの承認なしに通常のPRをmergeしないでください。
- Issueを手動でcloseしないでください。
- Dependabot PRを自動Approveしないでください。Dependabot PRのauto-mergeは、repository ownerによるApprove、repositoryのauto-merge設定、CI / branch protection等の条件をすべて満たす場合にのみ有効になります。
