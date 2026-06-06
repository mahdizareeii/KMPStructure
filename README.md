🚀 Kotlin Multiplatform iOS XCFramework Build (No Mac Required)

This project demonstrates how to build an iOS XCFramework from a Kotlin Multiplatform (KMP) shared module using GitHub Actions, without needing a personal Mac.

📦 What this project solves

Normally, iOS builds require:

macOS
Xcode
Kotlin/Native toolchain

With this setup:

❌ No Mac required locally
❌ No Mac Cloud needed
✅ CI builds iOS XCFramework automatically
✅ Works on GitHub Actions macOS runners
⚙️ CI/CD Setup
🔹 Debug Build (develop)
run: ./gradlew :shared:assembleSharedDebugXCFramework
🔹 Release Build (master)
run: ./gradlew :shared:assembleSharedReleaseXCFramework

Artifacts are automatically uploaded from:

shared/build/XCFrameworks/
🧠 KMP Configuration

The shared module is configured to generate an XCFramework for:

iOS Device (arm64)
iOS Simulator (arm64)

Key setup:

Compose Multiplatform support
XCFramework aggregation
Static framework output
