// TODO do some source set black magic in the main project so we dont need this
plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("org.bytedeco.gradle-javacpp-platform") version "1.5.9"
}
repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("org.bytedeco:javacv-platform:1.5.5")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    // get by project property javacppPlatform
    val classifier = project.properties["javacppPlatform"] as String
    println("Classifier: $classifier")
    archiveClassifier.set(classifier)
    archiveFileName.set("JavaCv-$classifier.jar")
}