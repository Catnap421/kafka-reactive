package com.ausg.kafkareactive.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Mono
import reactor.kafka.receiver.ReceiverOffset
import reactor.kafka.receiver.ReceiverRecord

class Step4SubscriberTest {
    @Test
    fun test() {
        val subscriber = Step4Subscriber { Mono.just(true) }
        val offset = Mockito.mock(ReceiverOffset::class.java)
        Mockito.doNothing().`when`(offset).acknowledge()
        val record: ReceiverRecord<String, String> = Mockito.mock(ReceiverRecord::class.java) as ReceiverRecord<String, String>
        Mockito.`when`(record.key()).thenReturn("1")
        Mockito.`when`(record.value()).thenReturn("1")
        Mockito.`when`(record.receiverOffset()).thenReturn(offset)
        subscriber.hookOnNext(record)
    }
}