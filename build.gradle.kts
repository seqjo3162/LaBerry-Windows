plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"

    id("org.jetbrains.compose") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)

    // Compose UI stack
    implementation(compose.foundation)
    implementation(compose.animation)
    implementation(compose.material3)
    implementation(compose.runtime)
    implementation(compose.preview)
    implementation(compose.ui)

    implementation("org.jetbrains.compose.ui:ui-tooling:1.6.10")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Ktor client
    val ktor = "2.3.9"
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-logging:$ktor")
    implementation("io.ktor:ktor-client-websockets:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")

    implementation("org.slf4j:slf4j-simple:2.0.9")
}


compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
