import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.util.pipeline.PipelineContext
import module.auth.UserDataFull
import java.security.MessageDigest

fun String.getHashSHA256(): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(this.toByteArray())
    return digest.fold("") { str, it ->
        str + "%02x".format(it)
    }
}

fun Any.writeValueAsString(): String =
    jacksonObjectMapper().writeValueAsString(this)

fun String.trimAllSpaces(): String =
    Regex("\\s+").replace(this, " ").trim()

suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.getBody(): T {
    return jacksonObjectMapper().readValue(call.receive<String>())
}

suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.authUser(block: (user: UserDataFull, body: T) -> Unit) {
    try {
        val token = call.request.headers["AccessToken"] ?: throw UserUnauthorized()

        val userData = DatabaseClient.findUserById(token.split(":")[0].toInt()) ?: throw UserUnauthorized()

        println(TokenStore.createToken(userData))
        println(token)
        println(userData)

        if (TokenStore.createToken(userData) == token) {
            block(userData, getBody())
        } else {
            throw UserUnauthorized()
        }
    } catch (e: UserUnauthorized) {
        respondUnauthorized()
    }
}