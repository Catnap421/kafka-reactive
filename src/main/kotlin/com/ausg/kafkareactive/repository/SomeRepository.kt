package com.ausg.kafkareactive.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

interface SomeRepository {
    fun saveItem(item: Int): Mono<Int>
    fun getReceivers(itemNo: Int): Flux<Tuple2<Int, String>>
    fun notify(target: Tuple2<Int, String>): Mono<Tuple2<String, Boolean>>
    fun notifyMulti(target: Tuple2<List<Int>, String>): Mono<Tuple2<String, Boolean>>
    fun saveResult(result: Tuple2<String, Boolean>): Mono<Boolean>
}
