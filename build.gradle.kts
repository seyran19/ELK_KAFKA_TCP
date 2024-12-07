plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("ch.qos.logback:logback-classic:1.4.12")

    implementation("org.apache.kafka:kafka-clients:3.4.0")
//    implementation("org.springframework.cloud:spring-cloud-starter-zipkin:2.2.8.RELEASE")
    // https://mvnrepository.com/artifact/io.micrometer/micrometer-tracing-bridge-brave
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.4.0")
//     https://mvnrepository.com/artifact/io.zipkin.reporter2/zipkin-reporter-brave
    implementation("io.zipkin.reporter2:zipkin-reporter-brave:3.4.2")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.0")
    // https://mvnrepository.com/artifact/io.micrometer/micrometer-tracing-bridge-brave
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.4.0")








}

tasks.withType<Test> {
    useJUnitPlatform()
}
