package com.ausg.kafkareactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaReactiveApplication

fun main(args: Array<String>) {
    runApplication<KafkaReactiveApplication>(*args)
}
