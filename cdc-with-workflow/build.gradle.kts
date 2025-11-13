plugins {
    id("org.springframework.boot") version "3.2.0" // 스트링 부트의 자동 설정, 의존성 관리
    id("io.spring.dependency-management") version "1.1.4" // 의존성 버전을 따로 명시하지 않아도, Spring Bom을 기반으로 버전 관리
    kotlin("jvm") version "2.1.10" // Kotlin 코드를 JVM 바이트코드로 컴파일을 지원
    kotlin("plugin.spring") version "2.1.10" // Spring Boot 프레임워크와 Kotlin의 궁합을 맞춰주는 역할
    // -> 스프링에 빈을 등록을 할 떄, Kotlin은 기본적으로 Class가 final이다, Spring AOP (open) 자동으로 open 키워드를 적용
}

group = "org.ktor_lecture"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starter 패키지 ( JPA는 불필요하니 제거 )
    implementation("org.springframework.boot:spring-boot-starter") // 스프링 컨텍스트, 자동 설정, 기본 유틸
    implementation("org.springframework.boot:spring-boot-starter-logging") // Sl4fj

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // Jackson에서 Kotlin 데이터 클래스
    // -> data class 자동 처리, null 처리, default

    implementation("org.jetbrains.kotlin:kotlin-reflect") // Kotlin 리플렉션 기능
    // -> 런타임에서 클래스/메서드/프로퍼티 이런정보들을 읽고 다루는데에 활용 (빈 등록, Jackson 직렬화)

    implementation("org.springframework.kafka:spring-kafka") // Spring 환경에서 kafka를 활용하기 위한 라이브러리
    // -> Kafka 프로듀서, 컨슈머 설정, 리스너, 메시지

    implementation("io.temporal:temporal-sdk:1.20.1") // 클라이언트 SDK
    // -> 워크 플로우 정의, 액티비티를 실행, 에러 핸들링 기능을 제공
    implementation("io.temporal:temporal-spring-boot-starter-alpha:1.20.1") // Temporal을 Spring Boot와 통합
    // -> 빈 설정 자동화, 워크 플로우를 등록하는 과정을 간략하게 할 수 있도록 지원

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    // -> Java8 이상에서 제공되는 날짜나 시간 API를 처리할 수 있게 지원
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}