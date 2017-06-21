package com.epages.ngsampleapp

import org.springframework.stereotype.Component

@Component
class TokenRepository {

    var token: Token? = null
}

data class Token(val apiUrl: String, val token: String, val refreshToken: String)

