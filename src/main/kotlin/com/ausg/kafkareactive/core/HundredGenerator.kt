package com.ausg.kafkareactive.core

import reactor.core.publisher.Flux

interface HundredGenerator : SourceFluxGenerator {
    override fun generateSource(): Flux<Int> {
        return Flux.range(1, 100)
    }
}
