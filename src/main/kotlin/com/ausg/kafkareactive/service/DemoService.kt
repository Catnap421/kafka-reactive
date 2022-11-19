package com.ausg.kafkareactive.service

import com.ausg.kafkareactive.core.KafkaManager
import com.ausg.kafkareactive.core.SourceFluxGenerator
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderRecord

abstract class DemoService(var serviceName: String, kafkaManager: KafkaManager) : SourceFluxGenerator {
    var kafkaManager: KafkaManager
    var disposable: Disposable? = null

    init {
        this.kafkaManager = kafkaManager
    }

    fun start(): Mono<String> {
        consume()
        produce()
        return Mono.just("START")
    }

    open fun stop(): Mono<String> {
        dispose(disposable)
        return Mono.just("STOP")
    }

    protected fun dispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    protected abstract fun consume()
    private fun produce() {
        val records: Flux<SenderRecord<String, String, String>> = generateSource()
            .doOnNext { i -> logger.info("Create - {}", i) }
            .map { obj: Any -> obj.toString() }
            .map { i -> SenderRecord.create(ProducerRecord(serviceName, i, i), i) }
        kafkaManager.producer(records)
            .subscribe()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DemoService::class.java)
    }
}