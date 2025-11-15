plugins {
    kotlin("jvm") version "2.2.20"
}

group = "com.pawlowski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val koogVersion = "0.5.0"
    implementation("ai.koog:koog-agents:$koogVersion")
    implementation("net.sourceforge.plantuml:plantuml:1.2025.10")
    testImplementation(kotlin("test"))
    testImplementation("ai.koog:agents-test:$koogVersion")
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("io.kotest:kotest-assertions-core:6.0.4")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
