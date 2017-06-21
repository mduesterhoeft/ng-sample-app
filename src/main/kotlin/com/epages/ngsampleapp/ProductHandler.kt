package com.epages.ngsampleapp

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProductHandler(val tokenRepository: TokenRepository, val webClient: WebClient) {

    private val logger = KotlinLogging.logger {}

    fun getProducts(request: ServerRequest): Mono<ServerResponse> {
        val token = tokenRepository.token!!
        logger.info { "getting products using $token" }
        val response: Mono<ClientResponse> = webClient
                .get()
                .uri(token.apiUrl + "/api/product-management/products")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${token.token}")
                .exchange()

        return response.flatMap { r ->
            logger.info { "got products status is ${r.statusCode()}" }
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(r.bodyToMono<String>(), String::class.java)
        }
    }
}
