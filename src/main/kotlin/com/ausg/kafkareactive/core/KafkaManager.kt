package com.ausg.kafkareactive.core

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.reactivestreams.Publisher
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.receiver.ReceiverRecord
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

@Component
class KafkaManager {
    private val consumerProps: MutableMap<String, Any>
    private val producerProps: MutableMap<String, Any>

    init {
        val broker = EmbeddedKafkaBroker(1, false, 1)
        broker.afterPropertiesSet()
        val bootstrapServers = broker.brokersAsString
        consumerProps = HashMap()
        consumerProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        consumerProps[ConsumerConfig.CLIENT_ID_CONFIG] = "consumer"
        consumerProps[ConsumerConfig.GROUP_ID_CONFIG] = "group"
        consumerProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        consumerProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        producerProps = HashMap()
        producerProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        producerProps[ProducerConfig.CLIENT_ID_CONFIG] = "producer"
        producerProps[ProducerConfig.ACKS_CONFIG] = "all"
        producerProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        producerProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    }

    fun consumer(topic: String): Flux<ReceiverRecord<String, String>> {
        val options = ReceiverOptions.create<String, String>(consumerProps)
            .subscription(setOf(topic))
        return KafkaReceiver.create(options)
            .receive()
    }

    fun producer(publisher: Publisher<out SenderRecord<String, String, String>?>?): Flux<SenderResult<String>> {
        val options = SenderOptions.create<String, String>(producerProps)
        return KafkaSender.create(options)
            .send(publisher)
    }
}