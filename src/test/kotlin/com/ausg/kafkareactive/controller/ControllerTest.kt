package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono


class ControllerTest {
    @Test
    fun startAndStopTest() {
        val service: Step0Service = Mockito.mock(Step0Service::class.java)
        Mockito.`when`(service.start()).thenReturn(Mono.just("START"))
        Mockito.`when`(service.stop()).thenReturn(Mono.just("STOP"))
        val controller = Step0Controller(service)
        val testClient = WebTestClient.bindToController(controller)
            .build()
        testClient.get().uri("/step0/start")
            .exchange()
            .expectBody(String::class.java)
            .isEqualTo("START")
        testClient.get().uri("/step0/start")
            .exchange()
            .expectBody(String::class.java)
            .isEqualTo("Already Running")
        testClient.get().uri("/step0/stop")
            .exchange()
            .expectBody(String::class.java)
            .isEqualTo("STOP")
        testClient.get().uri("/step0/stop")
            .exchange()
            .expectBody(String::class.java)
            .isEqualTo("Not Running Now")
    }

    @Test
    fun step1ControllerTest() {
        val service: Step1Service = Mockito.mock(Step1Service::class.java)
        Mockito.`when`(service.start()).thenReturn(Mono.just("START"))
        val controller: DemoController = Step1Controller(service)
        controllerTest(1, controller)
    }

    @Test
    fun step2ControllerTest() {
        val service: Step2Service = Mockito.mock(Step2Service::class.java)
        Mockito.`when`(service.start()).thenReturn(Mono.just("START"))
        val controller: DemoController = Step2Controller(service)
        controllerTest(2, controller)
    }

    @Test
    fun step3ControllerTest() {
        val service: Step3Service = Mockito.mock(Step3Service::class.java)
        Mockito.`when`(service.start()).thenReturn(Mono.just("START"))
        val controller: DemoController = Step3Controller(service)
        controllerTest(3, controller)
    }

    @Test
    fun step4ControllerTest() {
        val service: Step4Service = Mockito.mock(Step4Service::class.java)
        Mockito.`when`(service.start()).thenReturn(Mono.just("START"))
        val controller: DemoController = Step4Controller(service)
        controllerTest(4, controller)
    }

    @Test
    fun step5ControllerTest() {
        val service: Step5Service = Mockito.mock(Step5Service::class.java)
        Mockito.`when`(service.start()).thenReturn(Mono.just("START"))
        val controller: DemoController = Step5Controller(service)
        controllerTest(5, controller)
    }

    private fun controllerTest(step: Int, controller: DemoController) {
        val testClient = WebTestClient.bindToController(controller)
            .build()
        testClient.get().uri("/step$step/start")
            .exchange()
            .expectBody(String::class.java)
            .isEqualTo("START")
    }
}
