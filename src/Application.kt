import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.Application
import io.ktor.routing.routing
import module.auth.authTokenGet
import module.project.projectEdit
import module.project.projectFindByParameters
import module.project.projectNew
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

val json = jacksonObjectMapper()

val configuration = json.readValue<ConfigurationFile>(File("./configuration"))

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        authTokenGet()
        userFind()
        projectNew()
        projectEdit()
        projectFindByParameters()
        taskNew()
        taskEdit()
        taskChangeStatus()
    }
}

data class ConfigurationFile(
    val databaseUrl: String,
    val databaseDriver: String,
    val databaseUser: String,
    val databasePassword: String
)
