buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.4.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")

    classpath("com.google.gms:google-services:4.4.1")
    classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.1")
    classpath("com.google.firebase:firebase-appdistribution-gradle:5.0.0")
    classpath("com.github.triplet.gradle:play-publisher:3.9.1")
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