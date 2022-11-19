package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.DemoService
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicBoolean

abstract class DemoController(service: DemoService) {
    private val service: DemoService
    private val running: AtomicBoolean

    init {
        this.service = service
        running = AtomicBoolean(false)
    }

    @GetMapping("/start")
    fun start(): Mono<String> {
        return if (running.compareAndSet(false, true)) service.start() else Mono.just("Already Running")
    }

    @GetMapping("/stop")
    fun stop(): Mono<String> {
        return if (running.compareAndSet(true, false)) service.stop() else Mono.just("Not Running Now")
    }
}