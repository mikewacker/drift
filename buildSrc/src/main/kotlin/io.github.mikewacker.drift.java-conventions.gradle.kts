import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-library`
    id("com.diffplug.spotless")
    id("net.ltgt.errorprone")
}

repositories {
    mavenCentral()
}

val libs = the<LibrariesForLibs>() // version catalog workaround for convention plugins

dependencies {
    errorprone(libs.errorprone.core)

    testImplementation(libs.assertj.core)
    testImplementation(libs.junit.jupiter.api)

    testRuntimeOnly(libs.junit.jupiter.engine)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("Werror", true)
}

spotless {
    java {
        palantirJavaFormat("2.39.0")
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:all,-processing,-serial", "-Werror"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
