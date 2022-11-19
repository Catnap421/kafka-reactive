package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.Step5Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/step5")
class Step5Controller(service: Step5Service) : DemoController(service)