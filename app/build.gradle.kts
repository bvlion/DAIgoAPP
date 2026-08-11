import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.google.services)
  alias(libs.plugins.firebase.crashlytics)
  alias(libs.plugins.firebase.appdistribution)
  alias(libs.plugins.play.publisher)
}

android {
  namespace = "net.ambitious.daigoapp.android"
  compileSdk = 37
  defaultConfig {
    minSdk = 26
    targetSdk = 37
    versionCode = 8
    versionName = "1.0.3"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }

    buildConfigField("String", "HOST", "\"${System.getenv("HOST") ?: ""}\"")
    buildConfigField("String", "BEARER", "\"${System.getenv("BEARER") ?: "test_test"}\"")
  }
  signingConfigs {
    create("release") {
      keyAlias = System.getenv("KEYSTORE_ALIAS")
      keyPassword = System.getenv("KEYSTORE_PASSWORD")
      storeFile = file("../release.keystore")
      storePassword = System.getenv("KEYSTORE_PASSWORD")
    }
  }
  buildTypes {
    release {
      isMinifyEnabled = true
      buildConfigField("String", "ADMOB_NATIVE_KEY", "\"${System.getenv("ANDROID_ADMOB_NATIVE_KEY")}\"")
      manifestPlaceholders["admob_key"] = System.getenv("ANDROID_ADMOB_KEY") ?: ""
      signingConfig = signingConfigs.getByName("release")
      firebaseAppDistribution {
        groups = "developer"
        artifactType = "APK"
        artifactPath = "app/build/outputs/apk/release/app-release.apk"
        releaseNotesFile = "note.txt"
      }
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
    }
    debug {
      isDebuggable = true
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
      buildConfigField("String", "ADMOB_NATIVE_KEY", "\"ca-app-pub-3940256099942544/2247696110\"")
      manifestPlaceholders["admob_key"] = "ca-app-pub-3940256099942544~3347511713"
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  buildFeatures {
    compose = true
    buildConfig = true
    viewBinding = true
    dataBinding = true
  }
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.fromTarget("17")
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.kotlinx.serialization)
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)

  implementation(libs.play.services.ads.lite)

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.ui.viewbinding)
  implementation(libs.androidx.compose.runtime.livedata)

  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)

  implementation(libs.androidx.activity.compose)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  testImplementation(libs.junit)
  testImplementation(platform(libs.okhttp.bom))
  testImplementation(libs.okhttp.mockwebserver)

  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.tooling)
}

play {
  track.set("production")
  serviceAccountCredentials.set(file("../google-play-service.json"))
}
