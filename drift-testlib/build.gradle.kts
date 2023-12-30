plugins {
    id("io.github.mikewacker.drift.java-conventions")
}

dependencies {
    api(project(":drift-api"))
    api("com.fasterxml.jackson.core:jackson-core")

    implementation("org.assertj:assertj-core")

    testImplementation(project(":drift-api"))
    testImplementation("com.fasterxml.jackson.core:jackson-annotations")
    testImplementation("com.fasterxml.jackson.core:jackson-core")
}
