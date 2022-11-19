package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.HundredGenerator
import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.RecordProcessor
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kafka.receiver.ReceiverRecord

@Service
class Step0Service(kafkaManager: KafkaManager) :
    OperatorDemoService<Boolean>("step-0", kafkaManager), RecordProcessor, HundredGenerator {
    public override fun consumer(consumerFlux: Flux<ReceiverRecord<String, String>>): Flux<Boolean> {
        return consumerFlux.map(this::commit)
    }
}
