import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.diffplug.spotless")
    id("net.ltgt.errorprone")
}

/* conventions */

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
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("Werror", true)
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

/* publishing */

group = "io.github.mikewacker.drift"
version = "0.0.2-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                url = "https://github.com/mikewacker/drift"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit/"
                    }
                }
                developers {
                    developer {
                        id = "mikewacker"
                        name = "Mike Wacker"
                        email = "11431865+mikewacker@users.noreply.github.com"
                        url = "https://github.com/mikewacker"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/mikewacker/drift.git"
                    developerConnection = "scm:git:ssh://github.com:mikewacker/drift.git"
                    url = "https://github.com/mikewacker/drift/tree/main"
                }
            }
        }
    }
    repositories {
        maven {
            name = "Ossrh"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                val ossrhUsername: String? by project
                val ossrhToken: String? by project
                username = ossrhUsername
                password = ossrhToken
            }
        }
    }
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
