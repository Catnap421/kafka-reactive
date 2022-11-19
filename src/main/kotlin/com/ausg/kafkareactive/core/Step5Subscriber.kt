package com.ausg.kafkareactive.core

import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Mono
import reactor.core.publisher.UnicastProcessor
import reactor.kafka.receiver.ReceiverRecord
import java.util.function.Function

class Step5Subscriber(private val runner: Function<ReceiverRecord<String, String>, Mono<Boolean>>) :
    BaseSubscriber<ReceiverRecord<String, String>>() {
    private val offsetProcessor = UnicastProcessor.create<ReceiverRecord<String, String>>()
    private val offsetSink = offsetProcessor.sink()

    init {
        offsetProcessor.publish()
            .autoConnect()
            .reduce(
                -1L
            ) { last: Long, r: ReceiverRecord<String, String> ->
                if (last < r.offset()) commit(
                    r
                ) else last
            }
            .subscribe()
    }

    private fun commit(record: ReceiverRecord<String, String>): Long {
        logger.info("[COMMIT] {}", record.value())
        record.receiverOffset().acknowledge()
        return record.offset()
    }

    override fun hookOnSubscribe(subscription: Subscription) {
        request(5)
    }

    override fun hookOnNext(record: ReceiverRecord<String, String>) {
        Mono.just(record)
            .flatMap(runner)
            .subscribe { r: Boolean? ->
                logger.info("Consume END - {}", record.value())
                offsetSink.next(record)
                request(1)
            }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Step5Subscriber::class.java)
    }
}