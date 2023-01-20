plugins {
  id("com.android.application")
  kotlin("android")
  id("com.google.firebase.appdistribution")
  kotlin("kapt")
  id("com.github.triplet.play")
}

android {
  compileSdk = 33
  defaultConfig {
    applicationId = "net.ambitious.daigoapp.android"
    minSdk = 26
    targetSdk = 33
    versionCode = 5
    versionName = "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
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
        artifactPath = "androidApp/build/outputs/apk/release/androidApp-release.apk"
        releaseNotesFile = "note.txt"
      }
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
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
  viewBinding.isEnabled = true
  dataBinding.isEnabled = true
}

dependencies {
  implementation(project(":shared"))

  implementation("androidx.core:core-ktx:1.9.0")
  implementation("com.google.android.gms:play-services-ads-lite:21.2.0")
  val composeVersion = captureVersion(implementation("androidx.compose.ui:ui:1.1.1")!!)
  implementation("androidx.compose.material:material:$composeVersion")
  implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
  implementation("androidx.compose.ui:ui-viewbinding:$composeVersion")
  implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")

  implementation("androidx.datastore:datastore-preferences:1.0.0")
  val roomVersion = captureVersion(implementation("androidx.room:room-runtime:2.4.3")!!)
  kapt("androidx.room:room-compiler:$roomVersion")

  @Suppress("GradleDependency") // WIP
  implementation("androidx.activity:activity-compose:1.4.0")

  implementation("com.google.accompanist:accompanist-flowlayout:0.24.7-alpha")

  implementation(platform("com.google.firebase:firebase-bom:31.2.0"))
  implementation("com.google.firebase:firebase-crashlytics-ktx")
  implementation("com.google.firebase:firebase-analytics-ktx")

  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
  debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}

apply(plugin = "com.google.gms.google-services")
apply(plugin = "com.google.firebase.crashlytics")

fun captureVersion(dependency: Dependency) = dependency.version

play {
  track.set("production")
  serviceAccountCredentials.set(file("../google-play-service.json"))
}