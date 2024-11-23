plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
    id("dagger.hilt.android.plugin")
//    id("maven-publish")
    id("signing")

    id("com.vanniktech.maven.publish") version "0.28.0"
    id("com.gradleup.nmcp") version "0.0.7"
}

group = "com.sandymist.android.common.utilities"
version = "0.0.1"

android {
    namespace = "com.sandymist.android.common.utilities"
    compileSdk = 34

    defaultConfig {
        minSdk = 25

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "consumer-rules.pro"))
        }

        named("debug") {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        allWarningsAsErrors = true
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.compose)

//    // firebase
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.auth)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // logging
    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

afterEvaluate {

    configure<PublishingExtension> {
        publications.create<MavenPublication>("release") {
            groupId = "com.sandymist.android.common"
            artifactId = "utilities"
            version = rootProject.extra["projectVersion"] as String

            //afterEvaluate {
            from(
                components.findByName(rootProject.extra["publishComponent"] as String)
                    ?: error(">>>> SoftwareComponent not found")
            )
//        from(components[rootProject.extra["publishComponent"] as String])
            //}

            // POM Metadata
            pom {
                name.set("Utilities")
                description.set("A collection of utility functions for Android")
                url.set("https://github.com/saravr/android-utilities")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("saravr")
                        name.set("Sarav R")
                        email.set("saravr@yahoo.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/saravr/android-utilities.git")
                    developerConnection.set("scm:git:ssh://github.com/saravr/android-utilities.git")
                    url.set("https://github.com/saravr/android-utilities")
                }
            }
        }

        // Repositories
        repositories {
            mavenLocal()

            maven {
                name = "ossrh" // Sonatype OSSRH for Maven Central
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

                credentials {
                    username = project.findProperty("ossrhUsername") as String?
                        ?: System.getenv("OSSRH_USERNAME")
                    password = project.findProperty("ossrhPassword") as String?
                        ?: System.getenv("OSSRH_PASSWORD")
                    println("++++ username: $username")
                    println("++++ pass: $password")
                }
            }
        }
    }

// Signing Configuration
    configure<SigningExtension> {
        val secFile = project.findProperty("signing.secretKeyFile") as String?
        println("+++++ SEC FILE: $secFile")
        val contents = secFile?.let { file(it).readText() }
//        println("+++++ CONTENTS: $contents")
        val keyid = project.findProperty("signing.keyId") as String?
        println("+++++ KeyId: $keyid")
        val signinpass = project.findProperty("signing.password") as String?
        println("++++ sp: $signinpass")
        useInMemoryPgpKeys(
            keyid,
            contents,
            project.findProperty("signing.password") as String?
        )
        sign(publishing.publications["release"])
    }
}

tasks.register("printComponents") {
    doLast {
        println("++++ COMPS: " + project.components.size)
        project.components.forEach {
            println("++++++ Available component: ${it.name}")
        }
    }
}
