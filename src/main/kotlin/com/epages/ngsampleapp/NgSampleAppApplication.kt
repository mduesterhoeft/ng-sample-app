package com.epages.ngsampleapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class NgSampleAppApplication

fun main(args: Array<String>) {
    SpringApplication.run(NgSampleAppApplication::class.java, *args)
}
