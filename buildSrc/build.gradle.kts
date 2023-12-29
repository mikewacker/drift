plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.23.3")
    implementation("net.ltgt.errorprone:net.ltgt.errorprone.gradle.plugin:3.1.0")
}
