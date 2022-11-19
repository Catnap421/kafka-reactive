package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.RecordProcessor
import com.ausg.kafkareactive.core.Step4Subscriber
import org.springframework.stereotype.Service
import reactor.core.publisher.BaseSubscriber
import reactor.kafka.receiver.ReceiverRecord

@Service
class Step4Service(kafkaManager: KafkaManager) : SubscriberDemoService("step-4", kafkaManager),
    RecordProcessor {
    protected override val subscriber: BaseSubscriber<ReceiverRecord<String, String>>
        protected get() = Step4Subscriber(this::justTrueWithDelay)
}