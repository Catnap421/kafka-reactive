package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.RecordProcessor
import com.ausg.kafkareactive.core.Step5Subscriber
import org.springframework.stereotype.Service
import reactor.core.publisher.BaseSubscriber
import reactor.kafka.receiver.ReceiverRecord

@Service
class Step5Service(kafkaManager: KafkaManager) : SubscriberDemoService("step-5", kafkaManager),
    RecordProcessor {
    protected override val subscriber: BaseSubscriber<ReceiverRecord<String, String>>
        protected get() = Step5Subscriber(this::justTrueWithRandDelay)
}