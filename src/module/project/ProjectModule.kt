package module.project

import authUser
import io.ktor.routing.Routing
import io.ktor.routing.post
import respondSuccess

fun Routing.projectNew() {
    post("/project/new") {
        authUser<ProjectCreateI> { userData, body ->
            val project = DatabaseClient.createProject(body.data)

            respondSuccess(ProjectCreateO(project))
        }
    }
}
