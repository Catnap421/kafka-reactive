package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.repository.SomeRepository
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverOffset
import reactor.kafka.receiver.ReceiverRecord
import reactor.kafka.sender.SenderResult
import reactor.test.StepVerifier
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.time.Duration
import java.util.*
import java.util.function.Function

class Step3ServiceTest {
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
        Mockito.`when`(manager.consumer("step-3")).thenReturn(Flux.just(record))
        Mockito.`when`(manager.consumer("step-3-2")).thenReturn(Flux.just(record))
        val repository: SomeRepository = Mockito.mock(SomeRepository::class.java)
        Mockito.`when`(repository.saveItem(1)).thenReturn(Mono.just(1))
        Mockito.`when`(repository.getReceivers(1)).thenReturn(Flux.just(Tuples.of(1, "A")))
        Mockito.`when`(repository.notifyMulti(ArgumentMatchers.any())).thenReturn(Mono.empty())
        val service = Step3Service(manager, repository)
        StepVerifier.create(service.start())
            .expectNext("START")
            .verifyComplete()
        StepVerifier.create(service.stop())
            .expectNext("STOP")
            .verifyComplete()
    }

    @Test
    fun step3ConsumerTest() {
        val offset = Mockito.mock(ReceiverOffset::class.java)
        Mockito.doNothing().`when`(offset).acknowledge()
        val record1: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
        Mockito.`when`(record1.key()).thenReturn("1")
        Mockito.`when`(record1.value()).thenReturn("1")
        Mockito.`when`(record1.receiverOffset()).thenReturn(offset)
        val record2: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
        Mockito.`when`(record2.key()).thenReturn("2")
        Mockito.`when`(record2.value()).thenReturn("2")
        Mockito.`when`(record2.receiverOffset()).thenReturn(offset)
        val repository: SomeRepository = Mockito.mock(SomeRepository::class.java)
        Mockito.`when`(repository.saveItem(1)).thenReturn(Mono.just(1))
        Mockito.`when`(repository.getReceivers(1)).thenReturn(Flux.empty())
        Mockito.`when`(repository.saveItem(2)).thenReturn(Mono.just(2))
        Mockito.`when`(repository.getReceivers(2)).thenReturn(Flux.just(Tuples.of(2, "A")))
        val result: SenderResult<String> = Mockito.mock(SenderResult::class.java) as SenderResult<String>
        val kafkaManager: KafkaManager = Mockito.mock(KafkaManager::class.java)
        Mockito.`when`(kafkaManager.producer(ArgumentMatchers.any())).thenReturn(Flux.just(result))
        val service = Step3Service(kafkaManager, repository)
        StepVerifier.create(service.consumer(Flux.just(record1, record2)))
            .expectNext(result)
            .verifyComplete()
    }

//    @Test
//    fun samplingTest() {
//        val service = Step3Service(null, null)
//        StepVerifier.withVirtualTime {
//            Flux.just(1, 1, 1, 1, 1)
//                .groupBy(Function.identity())
//                .flatMap<Any?>(service::sampling)
//        }
//            .thenAwait(Duration.ofSeconds(5))
//            .expectNext(1)
//            .verifyComplete()
//    }

//    @Test
//    fun step3NotifyConsumerTest() {
//        val offset = Mockito.mock(ReceiverOffset::class.java)
//        Mockito.doNothing().`when`(offset).acknowledge()
//        val record1: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
//        Mockito.`when`(record1.key()).thenReturn("A")
//        Mockito.`when`(record1.value()).thenReturn("1")
//        Mockito.`when`(record1.receiverOffset()).thenReturn(offset)
//        val record2: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
//        Mockito.`when`(record2.key()).thenReturn("B")
//        Mockito.`when`(record2.value()).thenReturn("2")
//        Mockito.`when`(record2.receiverOffset()).thenReturn(offset)
//        val repository: SomeRepository = Mockito.mock(SomeRepository::class.java)
//        Mockito.`when`(repository.notifyMulti(ArgumentMatchers.any())).thenReturn(Mono.just(Tuples.of("", true)))
//        Mockito.`when`(repository.saveResult(Tuples.of("", true))).thenReturn(Mono.just(true))
//        val service = Step3Service(null, repository)
//        StepVerifier.create(service.notifyConsumer(Flux.just(record1, record2)))
//            .expectNext(true, true)
//            .verifyComplete()
//    }

//    @Test
//    fun bufferingTest() {
//        val repository: SomeRepository = Mockito.mock(SomeRepository::class.java)
//        Mockito.`when`(repository.notifyMulti(Tuples.of(Arrays.asList(1, 2, 3, 4, 5), "A")))
//            .thenReturn(Mono.just(Tuples.of("A", true)))
//        Mockito.`when`(repository.saveResult(Tuples.of("", true))).thenReturn(Mono.just(true))
//        val service = Step3Service(null, repository)
//        StepVerifier.withVirtualTime {
//            Flux.just(
//                Tuples.of("A", 1),
//                Tuples.of("A", 2),
//                Tuples.of("A", 3),
//                Tuples.of("A", 4),
//                Tuples.of("A", 5)
//            )
//                .groupBy { obj: Tuple2<String, Int> -> obj.t1 }
//                .flatMap<Any?>(service::buffering)
//        }
//            .thenAwait(Duration.ofSeconds(10))
//            .expectNext(Tuples.of("A", true))
//            .verifyComplete()
//    }
}