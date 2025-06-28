plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "com.breno.benchmark"
version = "1.0.0"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

application {
    mainClass.set("com.breno.benchmark.MainKt")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "com.breno.benchmark.MainKt"
    }
    configurations["runtimeClasspath"].forEach { file ->
        from(zipTree(file.absoluteFile))
    }
}