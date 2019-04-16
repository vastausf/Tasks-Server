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

data class UserData(
    val email: String,
    val login: String,
    val password: String,
    val lastName: String,
    val firstName: String,
    val middleName: String
)

data class UserDataFull(
    val id: Int,
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val email: String
)
