import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
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
    testImplementation(libs.junitJupiter.api)

    testRuntimeOnly(libs.junitJupiter.engine)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

spotless {
    java {
        palantirJavaFormat(libs.versions.plugin.spotless.palantir.get())
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:all,-processing,-serial", "-Werror"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
