package com.ausg.kafkareactive.core

import reactor.core.publisher.Flux

interface SourceFluxGenerator {
    fun generateSource(): Flux<Int>
}
