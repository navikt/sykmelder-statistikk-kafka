val logbackVersion = "1.4.14"
val junitJupiterVersion = "5.10.1"
val ktfmtVersion = "0.44"
val logstashEncoderVersion = "7.4"
val ktorVersion = "2.3.7"
val smCommonVersion = "2.0.8"
val coroutinesVersion = "1.7.3"
val jacksonVersion = "2.16.1"
val flywayVersion = "10.6.0"
val postgresVersion = "42.7.1"
val exposedVersion = "0.46.0"
val javaVersion = JavaVersion.VERSION_17
val testContainersVersion = "1.19.3"
val mockkVersion = "1.13.9"
val kluentVersion = "1.73"
val unleashedVersion = "9.2.0"

plugins {
    id("application")
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.diffplug.spotless") version "6.24.0"
}

group = "no.nav.sykmelderstatistikk"
version = "0.0.1"

application {
    mainClass.set("no.nav.sykmelderstatistikk.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    implementation("no.nav.helse:syfosm-common-kafka:$smCommonVersion")
    implementation("io.getunleash:unleash-client-java:$unleashedVersion")

    compileOnly("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion") {
        exclude(group = "commons-codec")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {

    compileKotlin {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = javaVersion.toString()
    }


    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        isZip64 = true
        mergeServiceFiles {
            setPath("META-INF/services/org.flywaydb.core.extensibility.Plugin")
        }
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "no.nav.sykmelderstatistikk.ApplicationKt",
                ),
            )
        }
    }

    test {
        useJUnitPlatform {}
        testLogging {
            events("skipped", "failed")
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }


    spotless {
        kotlin { ktfmt(ktfmtVersion).kotlinlangStyle() }
        check {
            dependsOn("spotlessApply")
        }
    }

}
