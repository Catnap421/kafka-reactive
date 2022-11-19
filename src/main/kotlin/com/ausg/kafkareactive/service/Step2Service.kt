package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.DelayedRepeatTenGenerator
import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.RecordProcessor
import com.ausg.kafkareactive.repository.SomeRepository
import org.reactivestreams.Publisher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.GroupedFlux
import reactor.kafka.receiver.ReceiverRecord
import java.time.Duration
import java.util.function.Function

@Service
class Step2Service(kafkaManager: KafkaManager, repository: SomeRepository) :
    OperatorDemoService<Boolean>("step-2", kafkaManager), RecordProcessor, DelayedRepeatTenGenerator {
    private val repository: SomeRepository

    init {
        this.repository = repository
    }

    override fun consumer(consumerFlux: Flux<ReceiverRecord<String, String>>): Flux<Boolean> {
        return consumerFlux.map(this::commitAndConvertToInteger)
            .groupBy(Function.identity())
            .flatMap(this::sampling)
            .flatMap(repository::saveItem)
            .flatMap(repository::getReceivers)
            .flatMap(repository::notify)
            .flatMap(repository::saveResult)
    }

    protected fun sampling(groupedFlux: GroupedFlux<Int, Int>): Flux<Int> {
        return groupedFlux.sampleFirst(Duration.ofSeconds(5))
    }
}
