import io.ktor.routing.Routing
import io.ktor.routing.post

fun Routing.userFind() {
    post("/user/find") {
        authUser<UserFindI> { user, body ->
            val users = DatabaseClient.findUserByParameters(body.offset, body.limit, body.parameters)

            respondSuccess(users)
        }
    }
}