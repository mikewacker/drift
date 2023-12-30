plugins {
    id("io.github.mikewacker.drift.java-conventions")
}

dependencies {
    api(libs.immutables.value.annotations)
    api(libs.jackson.annotations)
    api(libs.jackson.core)
    api(libs.jackson.databind)

    testImplementation(project(":drift-testlib"))
    testImplementation(libs.guava.testlib)
    testImplementation(libs.jackson.core)
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "Drift API"
            description = "Lightweight interfaces that define a JSON API."
        }
    }
}
