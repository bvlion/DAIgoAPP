# DAIgoAPP
D◯I 語 Client

<a href='https://play.google.com/store/apps/details?id=net.ambitious.daigoapp.android&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Google Play で手に入れよう' width=300 src='https://play.google.com/intl/ja/badges/static/images/badges/ja_badge_web_generic.png'/></a>

## 概要

- Androidスマートフォン専用アプリ
- application moduleは `app/` のみ
- backendは [bvlion/DAIgoAPI2](https://github.com/bvlion/DAIgoAPI2)

## 開発環境

* Java 17
* Android Studio
* リポジトリ同梱のGradle Wrapper

## Backend / ローカル開発

backendは [bvlion/DAIgoAPI2](https://github.com/bvlion/DAIgoAPI2)（PHP / Slim4）です。ローカルではDAIgoAPI2リポジトリで以下を実行して起動します（詳細はDAIgoAPI2のREADMEを参照）。

```
docker compose up composer
docker compose up slim -d
```

DAIgoAPP側は `HOST` / `BEARER` を環境変数からGradle経由で`BuildConfig`へ渡します。`HOST`が空の場合、Android emulatorからのアクセスを想定し `http://10.0.2.2:8080` を使用します（`ApiClient.resolveHost`）。値そのものは各自のローカル環境（`.envrc`等）で用意してください。

## Build / Test

* `./gradlew :app:assembleDebug`
* `./gradlew :app:testDebugUnitTest`
* `./gradlew :app:connectedCheck`（端末またはエミュレーターが必要）

release buildは署名用環境変数と後述の`release.keystore`が必要なため、通常は後述のCIから実行します。

## CI / 配布

* PR: `main`向けPull Requestで対象ファイル（`.kt` / `.kts` / `.xml` / `.properties` / `gradle/libs.versions.toml` / `.github/workflows/**`）が変更されると [`pr-ci.yaml`](.github/workflows/pr-ci.yaml) がdebug build・unit test・instrumentation testを実行します。
* 本番配信: GitHub Releaseが`published`になると [`release.yaml`](.github/workflows/release.yaml) が`bundleRelease`と`publishBundle`（Google Play `production` track）を実行します。
* Play Store掲載情報: listing対象ファイル（`app/src/main/play/listings/ja-JP/**`）が`main`へpushされると [`listing.yaml`](.github/workflows/listing.yaml) が`publishListing`を実行します。

## 秘密情報・設定ファイル

以下はリポジトリに含めず、各自の環境で用意します（`.gitignore`で除外済み）。

* `google-play-service.json`
* `.envrc`（ローカルの`HOST` / `BEARER`等）

release buildの署名には`KEYSTORE_ALIAS` / `KEYSTORE_PASSWORD`等の環境変数が必要です。`release.keystore`は既存の署名ファイルとしてリポジトリで管理されています。新しい秘密値・資格情報はコミットしないでください。

## キー管理
各ファイル Notion にて管理中

## お問い合わせファイル

Google Spread Sheet にてある程度は返信を自動化している。
https://docs.google.com/spreadsheets/d/1ZN3u9z5zsbmZky5cOgSt6SZ2q78CKhX0l7Se6tAOM8Y/edit
