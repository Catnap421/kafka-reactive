package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.Step2Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/step2")
class Step2Controller(service: Step2Service) : DemoController(service)