import org.gradle.testing.jacoco.tasks.JacocoReport
import java.time.Instant

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    id("jacoco")
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "br.com.learningwithme"
version = "0.0.1-SNAPSHOT"
description = "learningwithme"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["springModulithVersion"] = "1.4.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.modulith:spring-modulith-starter-core:1.3.0")
    implementation("org.springframework.modulith:spring-modulith-starter-jdbc")
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
    }
    testImplementation("org.testcontainers:junit-jupiter:1.19.0")
    testImplementation("org.testcontainers:postgresql:1.19.0")
    testImplementation("org.postgresql:postgresql:42.6.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    extensions.findByName("jacoco")?.let { /* noop, keeps configuration intact */ }
}

sourceSets {
    create("intTest") {
        java.srcDir("src/intTest/kotlin")
        resources.srcDir("src/intTest/resources")
        compileClasspath += sourceSets["main"].output + sourceSets["test"].output
        runtimeClasspath += output + compileClasspath
    }
}

configurations {
    named("intTestImplementation") {
        extendsFrom(configurations["testImplementation"])
    }
    named("intTestRuntimeOnly") {
        extendsFrom(configurations["testRuntimeOnly"])
    }
}

tasks.register<Test>("intTest") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.12"
}

pitest {
    junit5PluginVersion.set("1.2.1")
    targetClasses.set(listOf("br.com.learningwithme.learningwithme.modules.*.internal.core.*"))
    targetTests.set(listOf("br.com.learningwithme.learningwithme.*"))

    mutators.set(listOf("STRONGER"))
    outputFormats.set(listOf("XML", "HTML"))
    mutationThreshold.set(76)
    coverageThreshold.set(75)
    failWhenNoMutations.set(false)

    excludedClasses.set(
        listOf(
            "**Dto",
            "**DTO",
            "**Request",
            "**Response",
            "**Config",
            "**Configuration",
            "**Controller",
            "**Adapter",
            "**Application",
        ),
    )
}

// ------------------
// Jacoco merged report (fixed outputs)
// ------------------

tasks.register<JacocoReport>("jacocoMergedReport") {
    dependsOn("test")
    if (project.tasks.names.contains("intTest")) {
        dependsOn("intTest")
    }

    val execFiles =
        fileTree(layout.buildDirectory.dir("jacoco")) {
            include("**/*.exec")
            include("**/*.exec*")
        }

    executionData.setFrom(execFiles)

    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacocoMergedReport/report.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoMergedReport/html"))
    }

    sourceSets(sourceSets["main"])
}

// ------------------
// checkCoverage (robust XML parsing)
// ------------------

tasks.register("checkCoverage") {
    dependsOn("jacocoMergedReport")

    doLast {
        val xmlReport =
            layout.buildDirectory
                .file("reports/jacoco/jacocoMergedReport/report.xml")
                .get()
                .asFile
        if (!xmlReport.exists()) throw GradleException("❌ JaCoCo XML report not found: ${xmlReport.path}")

        val db =
            javax.xml.parsers.DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
        val doc = db.parse(xmlReport)

        val counters = doc.getElementsByTagName("counter")
        var covered = 0.0
        var missed = 0.0
        for (i in 0 until counters.length) {
            val node = counters.item(i)
            val attrs = node.attributes
            val typeNode = attrs.getNamedItem("type")
            if (typeNode != null && typeNode.nodeValue == "LINE") {
                covered = attrs.getNamedItem("covered").nodeValue.toDouble()
                missed = attrs.getNamedItem("missed").nodeValue.toDouble()
            }
        }

        val percent = if ((covered + missed) == 0.0) 0 else ((covered / (covered + missed)) * 100).toInt()

        println("📊 JaCoCo coverage = $percent%")

        if (percent < 75) {
            throw GradleException("❌ Cobertura abaixo de 75% ($percent%)")
        }
    }
}

// ------------------
// writeCoverageSnapshot (robust)
// ------------------

tasks.register("writeCoverageSnapshot") {
    dependsOn("jacocoMergedReport")

    doLast {
        val xmlReportFile =
            layout.buildDirectory
                .file("reports/jacoco/jacocoMergedReport/xml/report.xml")
                .get()
                .asFile

        if (!xmlReportFile.exists()) {
            println("⚠️ JaCoCo XML not found, skipping snapshot")
            return@doLast
        }

        val percent =
            (
                xmlReportFile
                    .readText()
                    .substringAfter("line-rate=\"")
                    .substringBefore("\"")
                    .toDouble() * 100
            ).toInt()

        val branch = System.getenv("GITHUB_REF_NAME") ?: "local"
        val timestamp = Instant.now().epochSecond

        val historyFile = layout.projectDirectory.file(".ci-history/history.json").asFile
        historyFile.parentFile.mkdirs()
        historyFile.appendText("""{"branch":"$branch","timestamp":$timestamp,"coverage":$percent}""" + "\n")

        println("📁 Saved coverage snapshot in ${historyFile.path}")
    }
}

// ------------------
// writePitSnapshot (robust)
// ------------------

tasks.register("writePitSnapshot") {
    dependsOn("pitest")

    doLast {
        // pit generates a folder build/reports/pitest/<timestamp>/index.html
        val pitIndex =
            fileTree(layout.buildDirectory.dir("reports/pitest")) {
                include("**/index.html")
            }.files.firstOrNull()

        val outDir = layout.projectDirectory.file(".ci-history").asFile
        outDir.mkdirs()
        val outFile = outDir.resolve("pit-summary.json")

        val mutationCoverage =
            if (pitIndex != null && pitIndex.exists()) {
                val content = pitIndex.readText()
                // crude extraction: find first occurrence of 'Mutation Coverage' and grab number before %
                val m = Regex("Mutation Coverage[^%>]*>(\\d+)").find(content)
                m?.groupValues?.get(1)?.toIntOrNull()
            } else {
                null
            }

        outFile.writeText("{" + "\"mutationCoverage\":${mutationCoverage ?: "null"}}")
        println("📁 Saved PIT snapshot in ${outFile.path}")
    }
}
