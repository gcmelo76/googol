plugins {
    id("java")
    id("application")
}

application {
    mainClass = "Barrel"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":common"))
    implementation("org.json:json:20240303")
}

tasks.test {
    useJUnitPlatform()
}
// manifest configs
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}