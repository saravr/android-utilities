import com.android.build.api.dsl.ApplicationBuildType

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    namespace = "com.sandymist.android.pulltorefresh"
    compileSdk = 34

    defaultConfig {
        namespace = "com.sandymist.android.pulltorefresh"
        minSdk = 24
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    sourceSets {
        all {
            java {
                setSrcDirs(listOf("src/main/kotlin"))
            }
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
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(platform(libs.androidx.compose.bom))
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui")
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-graphics")
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-tooling")
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("aar") {
        groupId = "com.sandymist.android"
        artifactId = "pulltorefresh"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components[rootProject.extra["publishComponent"] as String])
        }
    }

    repositories {
        mavenLocal()
    }
}