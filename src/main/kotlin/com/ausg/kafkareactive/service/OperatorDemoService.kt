package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.KafkaManager
import reactor.core.publisher.Flux
import reactor.kafka.receiver.ReceiverRecord

abstract class OperatorDemoService<T>(serviceName: String, kafkaManager: KafkaManager) : DemoService(serviceName, kafkaManager) {
    protected abstract fun consumer(consumerFlux: Flux<ReceiverRecord<String, String>>): Flux<T>
    override fun consume() {
        disposable = kafkaManager.consumer(serviceName)
            .transformDeferred { consumerFlux: Flux<ReceiverRecord<String, String>> -> consumer(consumerFlux) }
            .subscribe()
    }
}