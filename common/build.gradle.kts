import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

group = "com.sergiobelda.todometer.common"
version = "1.0"

repositories {
    google()
}

// TODO Remove this block when https://youtrack.jetbrains.com/issue/KT-43944 resolved
android {
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    ios {
        binaries {
            framework {
                baseName = "common"
            }
        }
    }
    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Libs.Koin.core)
                api(Libs.Koin.test)

                implementation(Libs.SqlDelight.coroutines)
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.2.0")
                api("androidx.core:core-ktx:1.3.2")

                implementation(Libs.SqlDelight.androidDriver)

                implementation(Libs.AndroidX.DataStore.preferences)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(Libs.SqlDelight.jvmDriver)
            }
        }
        val desktopTest by getting
        val iosMain by getting {
            dependencies {
                implementation(Libs.SqlDelight.nativeDriver)
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

sqldelight {
    database("TodometerDatabase") {
        packageName = "com.sergiobelda.todometer"
    }
}