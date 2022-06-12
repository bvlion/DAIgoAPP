plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  compileSdk = 31
  defaultConfig {
    applicationId = "net.ambitious.daigoapp.android"
    minSdk = 26
    targetSdk = 31
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }
  buildTypes {
    release {
      isMinifyEnabled = true
      buildConfigField("String", "ADMOB_NATIVE_KEY", "\"${System.getenv("ANDROID_ADMOB_NATIVE_KEY")}\"")
      manifestPlaceholders["admob_key"] = System.getenv("ANDROID_ADMOB_KEY") ?: ""
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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.1.1"
  }
  packagingOptions {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }
}

dependencies {
  implementation(project(":shared"))

  implementation("androidx.core:core-ktx:1.8.0")
  implementation("com.google.android.gms:play-services-ads-lite:21.0.0")
  val composeVersion = captureVersion(implementation("androidx.compose.ui:ui:1.1.1")!!)
  implementation("androidx.compose.material:material:$composeVersion")
  implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
  implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
  implementation("androidx.activity:activity-compose:1.4.0")
  implementation("com.google.accompanist:accompanist-flowlayout:0.24.7-alpha")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
  debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}

fun captureVersion(dependency: Dependency) = dependency.version