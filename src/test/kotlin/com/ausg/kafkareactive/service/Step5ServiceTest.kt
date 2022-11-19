package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.KafkaManager
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.kafka.receiver.ReceiverOffset
import reactor.kafka.receiver.ReceiverRecord
import reactor.test.StepVerifier

class Step5ServiceTest {
    @Test
    fun test() {
        val offset = Mockito.mock(ReceiverOffset::class.java)
        Mockito.doNothing().`when`(offset).acknowledge()
        val record: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
        Mockito.`when`(record.key()).thenReturn("1")
        Mockito.`when`(record.value()).thenReturn("1")
        Mockito.`when`(record.receiverOffset()).thenReturn(offset)
        val manager: KafkaManager = Mockito.mock(KafkaManager::class.java)
        Mockito.`when`(manager.producer(ArgumentMatchers.any())).thenReturn(Flux.empty())
        Mockito.`when`(manager.consumer("step-5")).thenReturn(Flux.just(record))
        val service = Step5Service(manager)
        StepVerifier.create(service.start())
            .expectNext("START")
            .verifyComplete()
        StepVerifier.create(service.stop())
            .expectNext("STOP")
            .verifyComplete()
    }
}