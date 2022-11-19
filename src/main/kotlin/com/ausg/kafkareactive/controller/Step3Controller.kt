package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.Step3Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/step3")
class Step3Controller(service: Step3Service) : DemoController(service)
