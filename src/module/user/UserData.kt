data class UserFindI(
    val offset: Int,
    val limit: Int,
    val parameters: UserDataSearch
)

data class UserFindO(
    val data: List<UserData>
)


data class UserData(
    val id: Int,
    val email: String,
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

data class UserDataSearch(
    val email: String? = null,
    val lastName: String? = null,
    val firstName: String? = null,
    val middleName: String? = null
)
