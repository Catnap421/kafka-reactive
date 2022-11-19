package com.ausg.kafkareactive.core

import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord
import java.util.function.Function

class Step4Subscriber(private val runner: Function<ReceiverRecord<String, String>, Mono<Boolean>>) :
    BaseSubscriber<ReceiverRecord<String, String>>() {
    override fun hookOnSubscribe(subscription: Subscription) {
        request(3)
    }

    override fun hookOnNext(record: ReceiverRecord<String, String>) {
        Mono.just(record)
            .flatMap(runner)
            .subscribe { r: Boolean? ->
                logger.info("[COMMIT] Consume END - {}", record.value())
                record.receiverOffset().acknowledge()
                request(1)
            }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Step4Subscriber::class.java)
    }
}
