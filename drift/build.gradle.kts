plugins {
    `java-library`
    id("io.github.mikewacker.drift.java-conventions")
    id("io.github.mikewacker.drift.publish-conventions")
}

dependencies {
    api(project(":drift-api"))
    api(libs.jackson.core)
    api(libs.okhttp3.okhttp)
    api(libs.undertow.core)

    implementation(libs.xnio.api)

    testImplementation(project(":drift-api"))
    testImplementation(project(":drift-testlib"))
    testImplementation(libs.jackson.core)
    testImplementation(libs.okhttp3.mockwebserver)
    testImplementation(libs.okio.jvm)
    testImplementation(libs.undertow.core)
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "Drift"
            description = "Lightweight, all-in-one library to rapidly prototype a JSON API."
        }
    }
}
