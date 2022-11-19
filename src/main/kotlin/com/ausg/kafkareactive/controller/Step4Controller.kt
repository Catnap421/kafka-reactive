package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.Step4Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/step4")
class Step4Controller(service: Step4Service) : DemoController(service)