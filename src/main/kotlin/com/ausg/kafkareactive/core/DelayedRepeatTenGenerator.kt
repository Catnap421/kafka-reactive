package com.ausg.kafkareactive.core

import reactor.core.publisher.Flux
import java.time.Duration

interface DelayedRepeatTenGenerator : SourceFluxGenerator {
    override fun generateSource(): Flux<Int> {
        return Flux.range(1, 10)
            .repeat(9)
            .delayElements(Duration.ofMillis(80))
    }
}
