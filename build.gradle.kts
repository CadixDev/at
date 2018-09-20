plugins {
    `java-library`
    signing
    `maven-publish`
    id("net.minecrell.licenser") version "0.4.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    api("me.jamiemansfield:bombe:0.3.0-SNAPSHOT")
    compileOnly("me.jamiemansfield:lorenz:0.5.0-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val sourceJar = task<Jar>("sourceJar") {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

val javadocJar = task<Jar>("javadocJar") {
    classifier = "javadoc"
    from(tasks["javadoc"])
}

artifacts {
    add("archives", sourceJar)
    add("archives", javadocJar)
}

val isSnapshot = version.toString().endsWith("-SNAPSHOT")

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(sourceJar)
            artifact(javadocJar)

            pom {
                val url: String by project
                url(url)

                scm {
                    url(url)
                    connection("scm:git:$url.git")
                    developerConnection.set(connection)
                }

                issueManagement {
                    system("GitHub Issues")
                    url("$url/issues")
                }

                licenses {
                    license {
                        name("MIT License")
                        url("https://opensource.org/licenses/MIT")
                        distribution("repo")
                    }
                }
            }
        }
    }

    repositories {
        val sonatypeUsername: String? by project
        val sonatypePassword: String? by project
        if (sonatypeUsername != null && sonatypePassword != null) {
            val url = if (isSnapshot) "https://oss.sonatype.org/content/repositories/snapshots/"
                else "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            maven(url) {
                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Sign> {
    onlyIf { !isSnapshot }
}

operator fun Property<String>.invoke(v: String) = set(v)
