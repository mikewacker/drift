plugins {
    `java-library`
    `java-test-fixtures`
    id("io.github.mikewacker.drift.java-conventions")
    id("io.github.mikewacker.drift.publish-conventions")
}

dependencies {
    // main
    api(project(":drift-api"))
    api(libs.errorprone.annotations)
    api(libs.jackson.core)
    api(libs.junitJupiter.api)
    api(libs.okhttp3.mockwebserver)
    api(libs.undertow.core)

    implementation(libs.assertj.core)

    // test fixtures
    testFixturesApi(libs.okhttp3.okhttp)

    // test
    testImplementation(project(":drift-api"))
    testImplementation(libs.jackson.annotations)
    testImplementation(libs.jackson.core)
    testImplementation(libs.okhttp3.mockwebserver)
    testImplementation(libs.okhttp3.okhttp)
    testImplementation(libs.undertow.core)
}

publishing {
    publications.named<MavenPublication>("mavenJava") {
        pom {
            name = "Drift Test Library"
            description = "Library that facilitates testing for a JSON API, using both unit tests and end-to-end tests."
        }
    }
}

// Don't publish test fixtures.
val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
javaComponent.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }
