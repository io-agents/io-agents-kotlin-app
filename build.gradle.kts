plugins {
    kotlin("jvm") version "2.2.20"
}

group = "com.pawlowski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ai.koog:koog-agents:0.5.0")
    implementation("net.sourceforge.plantuml:plantuml:1.2025.10")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
