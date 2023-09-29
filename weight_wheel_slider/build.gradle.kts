plugins {
    id("maven-publish")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

group = "com.ezinwa"
version = "1.0"

android {
    namespace = "com.ezinwa.circular_card_image"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
}

afterEvaluate {
    publishing {
        publications {
            val mavenJava by creating(MavenPublication::class) {
                from(components["release"])

                groupId = "com.ezinwa.weight_wheel_slider"
                artifactId = "weight_wheel_slider"
                version = "1.1.2"
            }
        }
    }
}