package com.epages.ngsampleapp

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router


@Configuration
class RouterConfiguration {

    @Bean
    fun authrorizeRouter(handler: AuthorizeCallbackHandler, productHandler: ProductHandler): RouterFunction<ServerResponse> = router {
        GET("/authorize-callback").invoke { r -> handler.processAuthorizeCallback(r) }
        GET("/products").invoke { r -> productHandler.getProducts(r) }
    }
}