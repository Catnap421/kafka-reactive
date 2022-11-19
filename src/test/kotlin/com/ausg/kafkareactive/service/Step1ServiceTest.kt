package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.repository.SomeRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverOffset
import reactor.kafka.receiver.ReceiverRecord
import reactor.test.StepVerifier
import reactor.util.function.Tuples

class Step1ServiceTest {
//    @Test
//    fun step1ConsumerTest() {
//        val offset = Mockito.mock(ReceiverOffset::class.java)
//        Mockito.doNothing().`when`(offset).acknowledge()
//        val record1: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
//        Mockito.`when`(record1.key()).thenReturn("1")
//        Mockito.`when`(record1.value()).thenReturn("1")
//        Mockito.`when`(record1.receiverOffset()).thenReturn(offset)
//        val record2: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
//        Mockito.`when`(record2.key()).thenReturn("2")
//        Mockito.`when`(record2.value()).thenReturn("2")
//        Mockito.`when`(record2.receiverOffset()).thenReturn(offset)
//        val repository: SomeRepository = Mockito.mock(SomeRepository::class.java)
//        Mockito.`when`(repository.saveItem(1)).thenReturn(Mono.just(1))
//        Mockito.`when`(repository.getReceivers(1)).thenReturn(Flux.empty())
//        Mockito.`when`(repository.saveItem(2)).thenReturn(Mono.just(2))
//        Mockito.`when`(repository.getReceivers(2)).thenReturn(Flux.just(Tuples.of(2, "A")))
//        Mockito.`when`(repository.notify(Tuples.of(2, "A"))).thenReturn(Mono.just(Tuples.of("A", true)))
//        Mockito.`when`(repository.saveResult(Tuples.of("A", true))).thenReturn(Mono.just(true))
//        val service = Step1Service(null, repository)
//        StepVerifier.create(service.consumer(Flux.just(record1, record2)))
//            .expectNext(true)
//            .verifyComplete()
//    }
}