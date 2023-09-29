import com.github.gradle.node.npm.task.NpxTask

plugins {
    java
    id("io.freefair.lombok") version "8.3"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.bytedeco.gradle-javacpp-platform") version "1.5.9"
    id("com.github.node-gradle.node") version "7.0.0"
}

group = "dev.badbird.picar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.bytedeco:javacv-platform:1.5.5")

    implementation("io.javalin:javalin:5.6.1")
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

val yarnInstall by tasks.creating(NpxTask::class) {
    workingDir.set(file("src/main/frontend"))
    command.set("yarn")
    args.set(listOf("install"))
}
// Define a task to build the React app
val buildReactApp by tasks.creating(NpxTask::class) {
    workingDir.set(file("src/main/frontend"))
    command.set("yarn")
    args.set(listOf("run", "build"))
}
val devFrontend by tasks.creating(NpxTask::class) {
    workingDir.set(file("src/main/frontend"))
    command.set("yarn")
    args.set(listOf("run", "dev"))
}

// Make the JAR task depend on the React build task
tasks.shadowJar {
    archiveFileName.set("PiCar.jar")
    manifest {
        attributes["Main-Class"] = "dev.badbird.picar.Main"
    }
    dependsOn("buildReactApp")

    // Include the React build output in the JAR resources
    from("src/main/frontend/dist") {
        into("frontend") // Adjust this path as needed
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
