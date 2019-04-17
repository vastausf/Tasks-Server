package module.project

import authUser
import io.ktor.routing.Routing
import io.ktor.routing.post
import respondForbidden
import respondNotFound
import respondSuccess

fun Routing.projectNew() {
    post("/project/new") {
        authUser<ProjectNewI> { userData, body ->
            val project = DatabaseClient.createProject(userData.id, body.data)

            respondSuccess(ProjectNewO(project))
        }
    }
}

fun Routing.projectEdit() {
    post("/project/edit") {
        authUser<ProjectEditI> { userData, body ->
            if (DatabaseClient.canEditProject(userData.id, body.id)) {
                val project = DatabaseClient.editProject(body.id, body.newData)

                if (project != null) {
                    respondSuccess(ProjectEditO(project))
                } else {
                    respondNotFound()
                }
            } else {
                respondForbidden()
            }
        }
    }
}

fun Routing.projectFindByParameters() {
    post("/project/find") {
        authUser<ProjectFindI> { userData, body ->
            val projects = DatabaseClient.findProjectByParameters(body.offset, body.limit, body.parameters, userData.id)

            respondSuccess(ProjectFindO(projects))
        }
    }
}
