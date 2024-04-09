import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization") version "1.6.10"
  id("com.android.library")
  id("com.codingfeline.buildkonfig")
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
    val commonMain by getting {
      dependencies {
        val ktorVersion = captureVersion(implementation("io.ktor:ktor-client-core:1.6.8")!!)
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }
    val androidMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-android:1.6.8")
      }
    }
    val androidTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation("junit:junit:4.13.2")
      }
    }
    val iosMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-ios:2.3.10")
      }
    }
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

buildkonfig {
  packageName = "net.ambitious.daigoapp"
  defaultConfigs {
    buildConfigField(STRING, "host", System.getenv("HOST") ?: "")
    buildConfigField(STRING, "bearer", System.getenv("BEARER") ?: "test_test")
  }
}

fun captureVersion(dependency: Dependency) = dependency.version