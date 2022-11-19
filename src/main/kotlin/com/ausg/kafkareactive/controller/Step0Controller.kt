package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.Step0Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/step0")
class Step0Controller(service: Step0Service) : DemoController(service)
