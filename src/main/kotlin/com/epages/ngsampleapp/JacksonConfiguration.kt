package com.epages.ngsampleapp

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class JacksonConfiguration {

    @Bean
    fun kotlinJacksonModule() = KotlinModule()
}