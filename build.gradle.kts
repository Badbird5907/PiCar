plugins {
    java
    id("io.freefair.lombok") version "8.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.badbird.picar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    // implementation("com.github.sarxos:webcam-capture:0.3.12") // {
        // exclude bridj
        // exclude(group = "com.nativelibs4java", module = "bridj")
    // }
    // implementation("com.nativelibs4java:bridj:0.6.3-SNAPSHOT")
    // implementation(files("libs/bridj-0.6.3-SNAPSHOT.jar"))
    // implementation(files("libs/bridj-0.8.0.jar"))

    // implementation("com.github.sarxos:webcam-capture-driver-raspberrypi:0.3.13-SNAPSHOT")

    implementation("org.bytedeco:javacv-platform:1.5.5")

    implementation("io.javalin:javalin:5.6.1")
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

tasks {
    shadowJar {
        archiveFileName.set("PiCar.jar")
        manifest {
            attributes["Main-Class"] = "dev.badbird.picar.Main"
        }
    }
    build {
        dependsOn(shadowJar)
    }
}