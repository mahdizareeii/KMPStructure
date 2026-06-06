import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    val xcFramework = XCFramework()

    listOf(
        //simulator on intel mac
        //iosX64(),

        //iphone ipad real devices
        iosArm64(),

        //simulator on apple silicon macs
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            export(libs.compose.runtime)
            export(libs.compose.foundation)
            export(libs.compose.material3)
            export(libs.compose.ui)
            export(libs.compose.components.resources)
            export(libs.androidx.lifecycle.viewmodelCompose)
            export(libs.androidx.lifecycle.runtimeCompose)

            xcFramework.add(this)
            binaryOption("bundleId", libs.plugins.projectId.get().pluginId)

            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    androidLibrary {
        namespace = "org.example.project.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
        }
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.ui)
            api(libs.compose.components.resources)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)
            api(libs.compose.uiToolingPreview)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
//
//tasks.register<Exec>("buildDebugXCFramework") {
//    val outputDir = layout.buildDirectory.dir("XCFrameworks/debug").get().asFile
//    val device = layout.buildDirectory.dir("bin/iosArm64/debugFramework/shared.framework").get().asFile
//    val simulator = layout.buildDirectory.dir("bin/iosSimulatorArm64/debugFramework/shared.framework").get().asFile
//
//    dependsOn(
//        "linkDebugFrameworkIosArm64",
//        "linkDebugFrameworkIosSimulatorArm64"
//    )
//
//    doFirst {
//        outputDir.deleteRecursively()
//        outputDir.mkdirs()
//    }
//
//    executable = "xcodebuild"
//    args = buildList {
//        add("-create-xcframework")
//
//        if (device.exists()) addAll(listOf("-framework", device.absolutePath))
//        if (simulator.exists()) addAll(listOf("-framework", simulator.absolutePath))
//
//        addAll(listOf("-output", "${outputDir.absolutePath}/shared.xcframework"))
//    }
//
//    doLast {
//        println("XCFramework created at: $outputDir")
//    }
//}