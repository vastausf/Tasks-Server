import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.respondUnauthorized() {
    call.respond(HttpStatusCode.Unauthorized)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondNotFound() {
    call.respond(HttpStatusCode.NotFound)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondSuccess(data: Any) {
    call.respond(HttpStatusCode.OK, data.writeValueAsString())
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondBadRequest() {
    call.respond(HttpStatusCode.BadRequest)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondForbidden() {
    call.respond(HttpStatusCode.Forbidden)
}
