plugins {
    `java-library`
    id("io.github.mikewacker.drift.java-conventions")
    id("io.github.mikewacker.drift.publish-conventions")
}

dependencies {
    // main
    api(libs.immutables.annotations)
    api(libs.jackson.annotations)
    api(libs.jackson.core)
    api(libs.jackson.databind)

    // test
    testImplementation(project(":drift-testlib"))
    testImplementation(libs.guava.testlib)
    testImplementation(libs.jackson.core)
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "Drift API"
            description = "Lightweight interfaces and utilities that define a JSON API."
        }
    }
}
