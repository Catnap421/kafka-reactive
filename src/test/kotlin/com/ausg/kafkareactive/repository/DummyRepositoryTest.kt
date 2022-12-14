package com.ausg.kafkareactive.repository

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.util.function.Tuples
import java.time.Duration

class DummyRepositoryTest {
    private val repository: SomeRepository = DummyRepository()
    @Test
    fun saveItemTest() {
        StepVerifier.withVirtualTime { repository.saveItem(1) }
            .thenAwait(Duration.ofSeconds(3))
            .expectNext(1)
            .verifyComplete()
    }

    @Test
    fun getReceiversTest(){
            StepVerifier.create(repository.getReceivers(1))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(2))
                .expectNext(Tuples.of(2, "조조"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(3))
                .expectNext(Tuples.of(3, "유비"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(4))
                .expectNext(Tuples.of(4, "조조"))
                .expectNext(Tuples.of(4, "손권"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(5))
                .expectNext(Tuples.of(5, "원소"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(6))
                .expectNext(Tuples.of(6, "조조"))
                .expectNext(Tuples.of(6, "유비"))
                .expectNext(Tuples.of(6, "여포"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(7))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(8))
                .expectNext(Tuples.of(8, "조조"))
                .expectNext(Tuples.of(8, "손권"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(9))
                .expectNext(Tuples.of(9, "유비"))
                .verifyComplete()
            StepVerifier.create(repository.getReceivers(10))
                .expectNext(Tuples.of(10, "조조"))
                .expectNext(Tuples.of(10, "원소"))
                .verifyComplete()
        }

    @Test
    fun notifyTest() {
        StepVerifier.withVirtualTime { repository.notify(Tuples.of(1, "홍길동")) }
            .thenAwait(Duration.ofSeconds(3))
            .expectNext(Tuples.of("홍길동", true))
            .verifyComplete()
    }

    @Test
    fun notifyMultiTest() {
        StepVerifier.withVirtualTime { repository.notifyMulti(Tuples.of(listOf(1, 2), "홍길동")) }
            .thenAwait(Duration.ofSeconds(3))
            .expectNext(Tuples.of("홍길동", true))
            .verifyComplete()
    }

    @Test
    fun saveResultTest() {
        StepVerifier.withVirtualTime { repository.saveResult(Tuples.of("홍길동", true)) }
            .thenAwait(Duration.ofSeconds(3))
            .expectNext(true)
            .verifyComplete()
    }
}