package com.epages.ngsampleapp

import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class AuthorizeCallbackHandler(val webClient: WebClient,
                               @Value("\${ng.sample.client.id}") val clientId: String,
                               @Value("\${ng.sample.client.secret}") val clientSecret: String,
                               val tokenRepository: TokenRepository) {

    private val logger = KotlinLogging.logger {}

    private val hmacAlgorithm = "HmacSHA1"

    fun processAuthorizeCallback(request: ServerRequest): Mono<ServerResponse> {
        val code = request.queryParam("code").orElseThrow({ IllegalArgumentException("parameter code missing") })
        val apiUrl = request.queryParam("api_url").orElseThrow({ IllegalArgumentException("parameter api_url missing") })
        val returnUrl = request.queryParam("return_url").orElseThrow({ IllegalArgumentException("parameter return_url missing") })
        val tokenUrl = request.queryParam("access_token_url").orElseThrow({ IllegalArgumentException("parameter access_token_url missing") })
        val signature = request.queryParam("signature").orElseThrow({ IllegalArgumentException("parameter signature missing") })

        logger.info { "received authorization request for tokenUri '$tokenUrl' " }
        logger.info { "using client id $clientId" }
        logger.info { "received signature '$signature' " }
        UriComponentsBuilder.fromUriString(tokenUrl)
        val base64 = String(Base64.getEncoder().encode("$clientId:$clientSecret".toByteArray(Charsets.UTF_8)))
        val response = webClient.post().uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64)
                .syncBody("code=$code&grant_type=authorization_code")
                .exchange().log()

        if (validateSignature(signature, request.uri().rawQuery, clientSecret)) {
            return response.flatMap { r ->
                logger.info { "token request status ${r.statusCode()}" }
                val tokenResponse: Mono<TokenResponse> = r.bodyToMono()
                tokenResponse.flatMap {t ->
                    tokenRepository.token = Token(apiUrl, t.accessToken, t.refreshToken)
                    logger.info { "token obtained for $apiUrl" }
                    ServerResponse.seeOther(URI.create(returnUrl)).build()
                }
            }
        } else {
            logger.error("signature invalid")
            return ServerResponse.badRequest().build()
        }
    }

    data class TokenResponse(
            @JsonProperty("access_token") val accessToken: String,
            @JsonProperty("refresh_token") val refreshToken: String
    )

    fun validateSignature(signature: String, requestQuery: String, clientSecret: String): Boolean {
        return signature == calculateHMAC(requestQuery, clientSecret)
    }

    private fun calculateHMAC(data: String, key: String): String {
        val signingKey = SecretKeySpec(key.toByteArray(), hmacAlgorithm)
        val mac = Mac.getInstance(hmacAlgorithm)
        mac.init(signingKey)

        return Base64.getEncoder().encodeToString(mac.doFinal(data.toByteArray()))
    }

}
