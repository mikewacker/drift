plugins {
    id("io.github.mikewacker.drift.java-conventions")
}

dependencies {
    api(project(":drift-api"))

    implementation("org.assertj:assertj-core")

    testImplementation(project(":drift-api"))
}
