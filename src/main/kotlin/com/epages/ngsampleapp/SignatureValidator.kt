package com.epages.ngsampleapp

import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object SignatureValidator {
    private val hmacAlgorithm = "HmacSHA1"

    fun validateSignature(signature: String, requestQuery: String, clientSecret: String): Boolean {
        return signature == calculateHMAC(requestQuery.substringBefore("&signature"), clientSecret)
    }

    private fun calculateHMAC(data: String, key: String): String {
        val signingKey = SecretKeySpec(key.toByteArray(), hmacAlgorithm)
        val mac = Mac.getInstance(hmacAlgorithm)
        mac.init(signingKey)

        return Base64.getEncoder().encodeToString(mac.doFinal(data.toByteArray()))
    }
}