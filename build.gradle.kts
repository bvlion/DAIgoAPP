buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.3.0")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")

    classpath("com.google.gms:google-services:4.3.15")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    classpath("com.google.firebase:firebase-appdistribution-gradle:4.1.0")
    classpath("com.github.triplet.gradle:play-publisher:3.8.4")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}