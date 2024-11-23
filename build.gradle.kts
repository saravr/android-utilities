// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
//    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.13" apply false
    id("com.vanniktech.maven.publish") version "0.28.0" apply false
    id("com.gradleup.nmcp") version "0.0.7" apply false
}

buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.kotlin.serialization)
    }
}

val projectVersion: String by extra("0.0.1")

private val publishVariant: String = project.findProperty("PUBLISH_VARIANT") as String? ?: "release"
val publishComponent: String by extra(publishVariant)

tasks.register("printProperties") {
    doLast {
        println("OSSRH Username: ${project.findProperty("ossrhUsername")}")
        println("Signing Key ID: ${project.findProperty("signing.keyId")}")
        println("Signing Key File: ${project.findProperty("signing.secretKeyFile")}")
    }
}
