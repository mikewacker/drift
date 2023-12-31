plugins {
    `java-library`
    id("io.github.mikewacker.drift.java-conventions")
    id("io.github.mikewacker.drift.publish-conventions")
}

dependencies {
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "Drift"
            description = "Lightweight, all-in-one library to rapidly prototype a JSON API."
        }
    }
}
