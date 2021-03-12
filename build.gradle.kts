plugins {
    kotlin("jvm") version "1.4.31"
    id("application")
    id("java")
    id("idea")
    id("java-library")

    // Creates fat JAR
    id("com.github.johnrengelman.shadow") version "6.1.0"

    // Adds dependencyUpdates task
    id("com.github.ben-manes.versions") version "0.38.0"
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

idea.module.setDownloadSources(true)
idea.module.setDownloadJavadoc(true)

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

val gradleDependencyVersion = "6.8.3"

application.mainClassName = "com.awslabs.iot.client.applications.AwsIotClientConsole"

tasks.wrapper {
    gradleVersion = gradleDependencyVersion
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
    maven(url = "https://jitpack.io")
    // Required for Gradle Tooling API
    maven(url = "https://repo.gradle.org/gradle/libs-releases-local/")
}

tasks.distZip { enabled = false }
tasks.distTar { enabled = false }
tasks.shadowDistZip { enabled = false }
tasks.shadowDistTar { enabled = false }

tasks.shadowJar {
    dependencies {
        // Get all of the API dependencies and exclude them
//        project.configurations.api.get().allDependencies.forEach { exclude(dependency(it)) }
    }

    // Create a shadow JAR with a specific name
    archiveName = "aws-iot-client.jar"
}

val daggerVersion = "2.33"
val jcommanderVersion = "1.81"
val awsSdk2Version = "2.16.17"
val gsonVersion = "2.8.6"
val slf4jVersion = "2.0.0-alpha1"
val vertxVersion = "4.0.2"
val jcabiLogVersion = "0.19.0"
val jlineVersion = "3.19.0"
val commonsLang3Version = "3.12.0"
val vavrVersion = "0.10.3"
val junitVersion = "4.13.2"
val resultsIteratorForAwsJavaSdkVersion = "11.0.7"
val awsIotCoreWebsocketsVersion = "0.6.3"
val jodahFailsafeVersion = "2.4.0"
val progressBarVersion = "0.9.0"

dependencies {
    // Dagger code generation
    annotationProcessor("com.google.dagger:dagger-compiler:$daggerVersion")
    testAnnotationProcessor("com.google.dagger:dagger-compiler:$daggerVersion")

    // Dependency injection with Dagger
    implementation("com.google.dagger:dagger:$daggerVersion")

    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("com.beust:jcommander:$jcommanderVersion")

    implementation("software.amazon.awssdk:greengrass:$awsSdk2Version")
    implementation("software.amazon.awssdk:iam:$awsSdk2Version")
    implementation("software.amazon.awssdk:iot:$awsSdk2Version")
    implementation("software.amazon.awssdk:iotdataplane:$awsSdk2Version")
    implementation("software.amazon.awssdk:cloudwatchlogs:$awsSdk2Version")
    implementation("software.amazon.awssdk:lambda:$awsSdk2Version")

    implementation("io.vertx:vertx-core:$vertxVersion")
    implementation("org.jline:jline:$jlineVersion")
    implementation("org.slf4j:slf4j-log4j12:$slf4jVersion")
    implementation("com.jcabi:jcabi-log:$jcabiLogVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("com.github.awslabs:results-iterator-for-aws-java-sdk:$resultsIteratorForAwsJavaSdkVersion")
    implementation("com.github.awslabs:aws-iot-core-websockets:$awsIotCoreWebsocketsVersion")

    implementation("io.vavr:vavr:$vavrVersion")
    implementation("net.jodah:failsafe:$jodahFailsafeVersion")
    implementation("me.tongfei:progressbar:$progressBarVersion")

    testImplementation("junit:junit:$junitVersion")

    // To force dependabot to update the Gradle wrapper dependency)
    testImplementation("org.gradle:gradle-tooling-api:$gradleDependencyVersion")
}
