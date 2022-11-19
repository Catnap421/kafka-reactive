package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.DelayedRepeatTenGenerator
import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.RecordProcessor
import com.ausg.kafkareactive.repository.SomeRepository
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Service
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.GroupedFlux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.time.Duration
import java.util.function.Function

@Service
class Step3Service(kafkaManager: KafkaManager, repository: SomeRepository) :
    OperatorDemoService<SenderResult<String>>("step-3", kafkaManager), RecordProcessor,
    DelayedRepeatTenGenerator {
    private val repository: SomeRepository
    private var notifyDisposable: Disposable? = null
    private val notifyServiceName: String

    init {
        this.repository = repository
        notifyServiceName = "step-3-2"
    }

    override fun consume() {
        notifyDisposable = kafkaManager.consumer(notifyServiceName)
            .transformDeferred(this::notifyConsumer)
            .subscribe()
        super.consume()
    }

    override fun stop(): Mono<String> {
        dispose(notifyDisposable)
        return super.stop()
    }

    public override fun consumer(consumerFlux: Flux<ReceiverRecord<String, String>>): Flux<SenderResult<String>> {
        return consumerFlux.map(this::commitAndConvertToInteger)
            .groupBy(Function.identity())
            .flatMap(this::sampling)
            .flatMap(repository::saveItem)
            .flatMap(repository::getReceivers)
            .flatMap { t ->
                kafkaManager.producer(
                    Mono.just(
                        SenderRecord.create(
                            ProducerRecord(notifyServiceName, t.t2, t.t1.toString()),
                            t.t1.toString()
                        )
                    )
                )
            }
    }

    fun sampling(groupedFlux: GroupedFlux<Int, Int>): Flux<Int> {
        return groupedFlux.sampleFirst(Duration.ofSeconds(5))
    }

    fun notifyConsumer(consumerFlux: Flux<ReceiverRecord<String, String>>): Flux<Boolean> {
        return consumerFlux.map(this::commitAndConvertToTuple)
            .groupBy{ obj:  Tuple2<String, Int> -> obj.t1 }
            .flatMap(this::buffering)
            .flatMap(repository::saveResult)
    }

    fun buffering(groupedFlux: GroupedFlux<String, Tuple2<String, Int>>): Flux<Tuple2<String, Boolean>> {
        return groupedFlux.map{ obj: Tuple2<String, Int> -> obj.t2 }
            .buffer(Duration.ofSeconds(10))
            .flatMap { list: List<Int> -> repository.notifyMulti(Tuples.of(list, groupedFlux.key())) }
    }
}
