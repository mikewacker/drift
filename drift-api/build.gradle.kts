plugins {
    id("io.github.mikewacker.drift.java-conventions")
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("com.fasterxml.jackson.core:jackson-core")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("org.immutables:value-annotations")

    testImplementation(project(":drift-testlib"))
    testImplementation("com.fasterxml.jackson.core:jackson-core")
    testImplementation("com.google.guava:guava-testlib")
}
