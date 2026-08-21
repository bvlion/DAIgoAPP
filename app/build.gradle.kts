import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.play.publisher)
}

android {
  namespace = "net.ambitious.daigoapp.android"
  compileSdk = 37
  defaultConfig {
    minSdk = 26
    targetSdk = 37
    versionCode = 11
    versionName = "1.0.6"

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
      signingConfig = signingConfigs.getByName("release")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
    }
    debug {
      isDebuggable = true
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  buildFeatures {
    compose = true
    buildConfig = true
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

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.runtime.livedata)

  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)

  implementation(libs.androidx.activity.compose)

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
