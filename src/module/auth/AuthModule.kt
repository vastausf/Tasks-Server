package module.auth

import TokenStore
import getBody
import io.ktor.routing.Routing
import io.ktor.routing.post
import respondSuccess
import respondUnauthorized

fun Routing.authTokenGet() {
    post("/auth/token/get") {
        val userData = getBody<AuthTokenGetI>()

        val user = DatabaseClient.findUserByLoginAndPassword(userData.login, userData.password).firstOrNull()

        if (user != null) {
            respondSuccess(
                AuthTokenGetO(TokenStore.createToken(user))
            )
        } else {
            respondUnauthorized()
        }
    }
}
