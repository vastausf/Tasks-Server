import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.respondUnauthorized() {
    call.respond(HttpStatusCode.Unauthorized)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondSuccess(data: Any) {
    call.respond(HttpStatusCode.OK, data.writeValueAsString())
}