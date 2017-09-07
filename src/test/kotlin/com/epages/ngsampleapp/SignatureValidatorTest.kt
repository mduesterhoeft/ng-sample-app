package com.epages.ngsampleapp

import org.junit.Test
import kotlin.test.assertFalse


class SignatureValidatorTest {

    @Test
    fun should_validate_signature() {
        val signature = "d7JdSrkV4bDCxJVec8IiDV35Uk8="
        val query = "&access_token_url=https://md.beyondshop.cloud/api/auth/oauth/token&api_url=https://md.beyondshop.cloud&code=m3La8S&return_url=https://md.beyondshop.cloud/merchant-ui"
        val secret = "va87htsph2fh043qf2c27c5j0j"
        assertFalse {
            SignatureValidator.validateSignature(signature, query, secret)
        }
    }
}