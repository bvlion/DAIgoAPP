buildscript {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.2.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")

    classpath("com.google.gms:google-services:4.3.10")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.0")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}