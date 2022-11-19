package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.DelayedRepeatTenGenerator
import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.RecordProcessor
import com.ausg.kafkareactive.repository.SomeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kafka.receiver.ReceiverRecord

@Service
class Step1Service(kafkaManager: KafkaManager, repository: SomeRepository) :
    OperatorDemoService<Boolean>("step-1", kafkaManager), RecordProcessor, DelayedRepeatTenGenerator {
    private val repository: SomeRepository

    init {
        this.repository = repository
    }

    override fun consumer(consumerFlux: Flux<ReceiverRecord<String, String>>): Flux<Boolean> {
        return consumerFlux.map(this::commitAndConvertToInteger)
            .flatMap(repository::saveItem)
            .flatMap(repository::getReceivers)
            .flatMap(repository::notify)
            .flatMap(repository::saveResult)
    }
}
