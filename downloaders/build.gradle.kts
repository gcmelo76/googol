plugins {
    id("java")
    id("application")
}

application {
    mainClass = "Downloader"
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
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("org.json:json:20240303")
}

tasks.test {
    useJUnitPlatform()
}