package com.ausg.kafkareactive.repository

import com.ausg.kafkareactive.core.RandomNumberGenerator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.time.Duration


@Component
class DummyRepository : SomeRepository, RandomNumberGenerator {
    private val receiverRule: Flux<Tuple2<Int, String>>

    init {
        receiverRule = Flux.just(
            Tuples.of(2, "조조"),
            Tuples.of(3, "유비"),
            Tuples.of(4, "손권"),
            Tuples.of(5, "원소"),
            Tuples.of(6, "여포")
        )
    }

    override fun saveItem(item: Int): Mono<Int> {
        val delay = randomMilliSeconds
        return Mono.just(item)
            .delayElement(Duration.ofMillis(delay.toLong()))
            .doOnNext { i: Int? ->
                logger.info(
                    "이벤트 [{}]의 발생 감지!",
                    item
                )
            }
    }

    override fun getReceivers(itemNo: Int): Flux<Tuple2<Int, String>> {
        return receiverRule.filter { t: Tuple2<Int, String> -> itemNo % t.t1 == 0 }
            .map { t: Tuple2<Int, String> ->
                Tuples.of(
                    itemNo,
                    t.t2
                )
            }
    }

    override fun notify(notifyTarget: Tuple2<Int, String>): Mono<Tuple2<String, Boolean>> {
        val delay = randomMilliSeconds
        return Mono.just(Tuples.of(notifyTarget.t2, true))
            .delayElement(Duration.ofMillis(delay.toLong()))
            .doOnNext { t: Tuple2<String, Boolean>? ->
                logger.info(
                    "[{}] 에게 이벤트 [{}] 발생을 알림!",
                    notifyTarget.t2,
                    notifyTarget.t1
                )
            }
    }

    override fun notifyMulti(notifyTarget: Tuple2<List<Int>, String>): Mono<Tuple2<String, Boolean>> {
        val delay = randomMilliSeconds
        return Mono.just(Tuples.of(notifyTarget.t2, true))
            .delayElement(Duration.ofMillis(delay.toLong()))
            .doOnNext { t: Tuple2<String, Boolean>? ->
                logger.info(
                    "[{}] 에게 이벤트 {} 발생을 알림!",
                    notifyTarget.t2,
                    notifyTarget.t1
                )
            }
    }

    override fun saveResult(result: Tuple2<String, Boolean>): Mono<Boolean> {
        val delay = randomMilliSeconds
        return Mono.just(true)
            .delayElement(Duration.ofMillis(delay.toLong()))
            .doOnNext { b: Boolean? ->
                logger.info(
                    "[{}] 에게 통지한 이력을 저장!",
                    result.t1
                )
            }
    }

    private val randomMilliSeconds: Int
        private get() = getRandomRange(500, 3000)

    companion object {
        private val logger = LoggerFactory.getLogger(DummyRepository::class.java)
    }
}