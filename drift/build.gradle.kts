plugins {
    `java-library`
    `java-test-fixtures`
    id("io.github.mikewacker.drift.java-conventions")
    id("io.github.mikewacker.drift.publish-conventions")
}

dependencies {
    // main
    api(project(":drift-api"))
    api(libs.jackson.core)
    api(libs.undertow.core)

    implementation(libs.okhttp3.okhttp)
    implementation(libs.xnio.api)

    // test fixtures
    testFixturesApi(project(":drift-api"))
    testFixturesApi(libs.undertow.core)

    testFixturesImplementation(project(":drift-testlib"))
    testFixturesImplementation(libs.assertj.core)
    testFixturesImplementation(libs.jackson.core)

    // test
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
