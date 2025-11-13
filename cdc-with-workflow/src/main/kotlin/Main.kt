package org.ktor_lecture

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class CdcDemoApplication

fun main(args : Array<String>) {
    runApplication<CdcDemoApplication>(*args)
}