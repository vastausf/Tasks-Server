package module.auth

data class AuthTokenGetI(
    val login: String,
    val password: String
)

data class AuthTokenGetO(
    val token: String
)

data class AuthData(
    val login: String,
    val password: String
)
