plugins {
    id("io.github.mikewacker.drift.java-conventions")
}

dependencies {
    api(project(":drift-api"))
    api(libs.jackson.core)

    implementation(libs.assertj.core)

    testImplementation(project(":drift-api"))
    testImplementation(libs.jackson.annotations)
    testImplementation(libs.jackson.core)
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "Drift Test Library"
            description = "Library that facilitates testing for a JSON API, using both unit tests and end-to-end tests."
        }
    }
}

