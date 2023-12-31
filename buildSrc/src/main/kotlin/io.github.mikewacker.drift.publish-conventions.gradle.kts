plugins {
    java
    `maven-publish`
    signing
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("Werror", true)
}

group = "io.github.mikewacker.drift"
version = "0.0.3-SNAPSHOT"

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