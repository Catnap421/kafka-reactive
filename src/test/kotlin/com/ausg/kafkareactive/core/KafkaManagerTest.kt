package com.ausg.kafkareactive.core

import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.kafka.sender.SenderRecord
import reactor.test.StepVerifier
import java.util.*
import java.util.function.Function
import java.util.function.Predicate

class KafkaManagerTest {
    @Test
    fun embeddedKafkaTest() {
        val manager = KafkaManager()
        StepVerifier.create(manager.producer(Flux.range(1, 5)
            .map { Objects.toString(it) }
            .map { SenderRecord.create(ProducerRecord("test", it, it), it) })
        )
            .expectNextMatches { it.correlationMetadata().equals("1") }
            .expectNextMatches { it.correlationMetadata().equals("2") }
            .expectNextCount(3)
            .verifyComplete()
        StepVerifier.create(manager.consumer("test").take(5))
            .expectNextMatches { it.value().equals("1") }
            .expectNextMatches { it.value().equals("2") }
            .expectNextMatches { it.value().equals("3") }
            .expectNextMatches { it.value().equals("4") }
            .expectNextMatches { it.value().equals("5") }
            .verifyComplete()
    }
}