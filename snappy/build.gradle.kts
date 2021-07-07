plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
    id("org.graalvm.buildtools.native") version "0.9.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.github.ajalt:clikt:1.3.0")
    implementation("org.xerial.snappy:snappy-java:1.1.8.4")
}

nativeBuild {
    // Define the main class for the application.
    mainClass.set("org.jackhammer2k.snappy.cli.SnappyKt")
}
