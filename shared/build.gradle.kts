import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import sun.jvmstat.monitor.MonitoredVmUtil.commandLine

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    val xcFramework = XCFramework()

    listOf(
        //iosX64(),
        iosArm64(),
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
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.uiToolingPreview)
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
//tasks.register("buildDebugXCFramework") {
//    val outputDirProvider = project.layout.buildDirectory.dir("XCFrameworks/debug")
//    val deviceFrameworkProvider = project.layout.buildDirectory.dir("bin/iosArm64/debugFramework/shared.framework")
//    val simulatorFrameworkProvider = project.layout.buildDirectory.dir("bin/iosSimulatorArm64/debugFramework/shared.framework")
//
//    doFirst {
//        val outputDir = outputDirProvider.get().asFile
//        outputDir.deleteRecursively()
//        outputDir.mkdirs()
//    }
//
//    dependsOn(
//        "linkDebugFrameworkIosArm64",
//        "linkDebugFrameworkIosSimulatorArm64"
//    )
//
//    doLast {
//        val outputDir = outputDirProvider.get().asFile
//        val deviceFramework = deviceFrameworkProvider.get().asFile
//        val simulatorFramework = simulatorFrameworkProvider.get().asFile
//
//        if (!deviceFramework.exists() && !simulatorFramework.exists()) {
//            throw GradleException("No frameworks found. Build frameworks first.")
//        }
//
//        val command = mutableListOf("xcodebuild", "-create-xcframework")
//        deviceFramework.takeIf { it.exists() }?.let { command += listOf("-framework", it.absolutePath) }
//        simulatorFramework.takeIf { it.exists() }?.let { command += listOf("-framework", it.absolutePath) }
//        command += listOf("-output", "${outputDir.absolutePath}/shared.xcframework")
//
//        project.exec {
//            commandLine(command)
//        }
//
//        println("Debug XCFramework created at: ${outputDir.absolutePath}/shared.xcframework")
//    }
//}