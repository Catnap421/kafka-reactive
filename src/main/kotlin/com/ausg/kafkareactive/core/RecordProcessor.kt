package com.ausg.kafkareactive.core

import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverRecord
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.time.Duration

interface RecordProcessor : RandomNumberGenerator {
    fun commit(record: ReceiverRecord<String, String>): Boolean {
        logger.info("Read - {}", record.value())
        record.receiverOffset().acknowledge()
        return true
    }

    fun commitAndConvertToInteger(record: ReceiverRecord<String, String>): Int {
        record.receiverOffset().acknowledge()
        return record.value().toInt()
    }

    fun commitAndConvertToTuple(record: ReceiverRecord<String, String>): Tuple2<String, Int> {
        record.receiverOffset().acknowledge()
        return Tuples.of(record.key(), record.value().toInt())
    }

    fun justTrue(record: ReceiverRecord<String, String>): Mono<Boolean> {
        logger.info("Consume START - {}", record.value())
        return Mono.just(true)
    }

    fun justTrueWithDelay(record: ReceiverRecord<String, String>): Mono<Boolean> {
        return justTrue(record)
            .delayElement(Duration.ofSeconds(1))
    }

    fun justTrueWithRandDelay(record: ReceiverRecord<String, String>): Mono<Boolean> {
        return justTrue(record)
            .delayElement(Duration.ofMillis(getRandomRange(500, 1500).toLong()))
    }

    companion object {
        val logger = LoggerFactory.getLogger(RecordProcessor::class.java)
    }
}