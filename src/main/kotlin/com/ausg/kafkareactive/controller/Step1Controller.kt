package com.ausg.kafkareactive.controller

import com.ausg.kafkareactive.service.Step1Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/step1")
class Step1Controller(service: Step1Service) : DemoController(service)
