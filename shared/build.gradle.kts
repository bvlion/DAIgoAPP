plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

kotlin {
  android()
  ios {
    binaries {
      framework {
        baseName = "shared"
      }
    }
  }
  sourceSets {
    val commonMain by getting
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val androidMain by getting
    val androidTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation("junit:junit:4.13.2")
      }
    }
    val iosMain by getting
    val iosTest by getting
  }
}

android {
  namespace = "net.ambitious.daigoapp.android"
  compileSdk = 33
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = 26
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
