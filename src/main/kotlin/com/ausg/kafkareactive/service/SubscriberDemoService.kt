package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.HundredGenerator
import com.ausg.kafkareactive.core.KafkaManager
import reactor.core.publisher.BaseSubscriber
import reactor.kafka.receiver.ReceiverRecord

abstract class SubscriberDemoService(serviceName: String, kafkaManager: KafkaManager) :
    DemoService(serviceName, kafkaManager), HundredGenerator {
    protected abstract val subscriber: BaseSubscriber<ReceiverRecord<String, String>>

    override fun consume() {
        val subscriber = subscriber
        kafkaManager.consumer(serviceName)
            .subscribe(subscriber)
        disposable = subscriber
    }
}