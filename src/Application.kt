import io.ktor.application.Application
import io.ktor.routing.routing
import module.auth.authTokenGet
import module.project.projectNew

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        authTokenGet()
        projectNew()
    }
}

