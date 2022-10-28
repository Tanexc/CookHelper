package ru.tanec.cookhelper.core.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.util.date.*
import java.util.*

object JwtTool {

    val config = HoconApplicationConfig(ConfigFactory.load())

    fun generateToken(
        name: String,
        surname: String,
        registered: Long,
        passwordHash: String
    ): String {
        return JWT.create()
            .withIssuer(config.property("jwt.issuer").toString())
            .withSubject(config.property("jwt.subject").toString())
            .withClaim("arg1", name)
            .withClaim("arg2", surname)
            .withClaim("arg3", registered)
            .withClaim("arg4", passwordHash)
            .withExpiresAt(Date(getTimeMillis() + 60*24*3600))
            .sign(Algorithm.HMAC256(config.property("jwt.secret").toString()))
    }

    fun String.isNotExpired() = JWT.decode(this).expiresAt.time > getTimeMillis()
}