plugins {
    id("io.github.mikewacker.drift.java-conventions")
}

dependencies {
    implementation(project(":drift-api"))
    implementation(project(":drift"))
    implementation(libs.jackson.core)
    implementation(libs.undertow.core)

    testImplementation(project(":drift-api"))
    testImplementation(project(":drift"))
    testImplementation(project(":drift-testlib"))
    testImplementation(libs.jackson.core)
    testImplementation(libs.okhttp3.mockwebserver)
    testImplementation(libs.undertow.core)
}
