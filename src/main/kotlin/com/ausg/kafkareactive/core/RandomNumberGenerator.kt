package com.ausg.kafkareactive.core

import java.util.*

interface RandomNumberGenerator {
    fun getRandomRange(start: Int, end: Int): Int {
        return Random()
            .ints(start, end)
            .findFirst()
            .orElse(start)
    }
}
