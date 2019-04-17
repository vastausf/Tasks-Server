import io.ktor.routing.Routing
import io.ktor.routing.post

fun Routing.taskNew() {
    post("/task/new") {
        authUser<TaskNewI> { userData, body ->

            if (DatabaseClient.canEditProject(userData.id, body.data.project)) {
                val task = DatabaseClient.createTask(userData.id, body.data)

                respondSuccess(TaskNewO(task))
            } else {
                respondForbidden()
            }
        }
    }
}

fun Routing.taskEdit() {
    post("/task/edit") {
        authUser<TaskEditI> { userData, body ->
            if (DatabaseClient.canEditTask(userData.id, body.id)) {
                val task = DatabaseClient.editTask(body.id, body.newData)

                if (task != null) {
                    respondSuccess(TaskEditO(task))
                } else {
                    respondNotFound()
                }
            } else {
                respondForbidden()
            }
        }
    }
}

fun Routing.taskChangeStatus() {
    post("/task/status/edit") {
        authUser<TaskStatusEditI> { userData, body ->
            if (DatabaseClient.canEditTask(userData.id, body.id)) {
                val task = DatabaseClient.editTaskStatus(body.id, userData.id, body.comment, body.newStatus)

                if (task != null) {
                    respondSuccess(TaskStatusEditO(task))
                } else {
                    respondNotFound()
                }
            } else {
                respondForbidden()
            }
        }
    }
}
