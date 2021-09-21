package com.ewan.ciboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CiBoardServerApplication

fun main(args: Array<String>) {
    runApplication<CiBoardServerApplication>(*args)
}
