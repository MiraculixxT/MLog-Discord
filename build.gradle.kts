plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

group = "de.miraculixx"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    val ktor_version: String by project

    implementation("net.dv8tion", "JDA", "5.0.0-beta.20")
    implementation("com.github.minndevelopment", "jda-ktx", "0.11.0-beta.19")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-rate-limit:$ktor_version")

    implementation("org.slf4j", "slf4j-nop", "2.0.0-alpha7")
    implementation("ch.qos.logback", "logback-classic", "1.2.11")

    implementation("org.mariadb.jdbc", "mariadb-java-client", "3.3.3")
}

application {
    mainClass.set("de.miraculixx.mlog.MLogBotKt")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}