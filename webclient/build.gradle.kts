plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

application {
    mainClass = "com.webclient.WebclientApplication"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.webjars:webjars-locator:0.52")
    implementation("org.webjars:sockjs-client:1.5.1")
    implementation("org.json:json:20240303")
    implementation(project(":common"))

}

tasks.test {
    useJUnitPlatform()
}



