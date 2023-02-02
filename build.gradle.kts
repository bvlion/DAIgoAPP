buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:7.4.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")

    classpath("com.google.gms:google-services:4.3.14")
    classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
    classpath("com.google.firebase:firebase-appdistribution-gradle:3.0.3")
    classpath("com.github.triplet.gradle:play-publisher:3.7.0")
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