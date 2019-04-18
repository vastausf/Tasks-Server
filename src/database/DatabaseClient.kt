import com.fasterxml.jackson.module.kotlin.readValue
import module.project.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object DatabaseClient {
    init {
        Database.connect(
            configuration.databaseUrl,
            configuration.databaseDriver,
            configuration.databaseUser,
            configuration.databasePassword
        )

        transaction {
            SchemaUtils.create(UsersTable, ProjectsTable, TasksTable)
        }

        //createUsers()
    }

    fun createUsers() {
        val users = listOf(
            "Fedoseev Avgustin Frolovich",
            "Volkov Mechislav Efimovich",
            "Nesterov Vadim Antonovich",
            "Kornilov Panteleimon Vladlenovich",
            "Naumov Ignatiy Igorevich",
            "Noskov Eldar Germannovich",
            "Rusakov Justin Vladlenovich",
            "Shiryaev Vsevolod Vladimirovich",
            "Fedorov Ruslan Platonovich",
            "Teterin Grigory Romanovich",
            "Osipov Semen Adolfovich",
            "Guryev Harry Bogdanovich",
            "Kirillov Azary Rostislavovich",
            "Doronin Emelyan Ulebovich",
            "Shashkov Venedikt Stepanovich",
            "Rusakov Avvakuum Gennadievich",
            "Subbotin Gennadiy Feliksovich",
            "Shiryaev Klim Eremeevich",
            "Savelyev Kondrat Veniaminovich",
            "Likhachev Yaroslav Romanovich",
            "Blokhin Evgeny Germanovich",
            "Abramov Vladimir Antonovich",
            "Nesterov Natan Khristoforovich",
            "Danilov Yermolai Svyatoslavovich",
            "Nekrasov Avvacum Evgenievich",
            "Nikitin Venedikt Rudolfovich",
            "Nekrasov Justinian Lukievich",
            "Likhachev Albert Fedoseevich",
            "Ustinov Eldar Lukyanovich",
            "Pakhomov Arthur Vyacheslavovich",
            "Gurev Avraam Fedoseevich",
            "Uvarov Justin Yaroslavovich",
            "Gavrilov Askold Stepanovich",
            "Konovalov Anatoly Filatovich",
            "Alekseev Ivan Efimovich",
            "Myasnikov Vsevolod Arsenevich",
            "Bolshakov Ustin Germanovich",
            "Uvarov Leonty Boguslavovich",
            "Sorokin Lavrenti Vadimovich",
            "Yudin Frol Eduardovich",
            "Timofeev Demyan Parfenyevich",
            "Timofeev Boleslav Grigorevich",
            "Ivanov Leonid Lukyanovich",
            "Galkin Ruslan Agafonovich",
            "Grigoriev Ignatiy Antonovich",
            "Egorov Vyacheslav Agafonovich",
            "Sobolev Naum Lukyanovich",
            "Ershov Mark Nikitevich",
            "Kudryavtsev Lavrenty Semenovich",
            "Evseev Kliment Kimovich"
        )

        transaction {
            users.map {
                val user = it.split(" ")

                listOf(user[0], user[1], user[2])
            }.forEach { (lastName, firstName, middleName) ->
                UserRow.new {
                    this.login = "$firstName.$lastName"
                    this.password = "$firstName$lastName$middleName".getHashSHA256()
                    this.firstName = firstName.capitalize()
                    this.lastName = lastName.capitalize()
                    this.middleName = middleName.capitalize()
                    this.email = "$firstName.$lastName@mail.com".decapitalize()
                }
            }
        }
    }

    fun findUserByLoginAndPassword(login: String, password: String): List<UserDataFull> = transaction {
        UserRow.find { (UsersTable.login eq login) and (UsersTable.password eq password) }.map {
            UserDataFull(
                it.id.value,
                it.login,
                it.password,
                it.lastName,
                it.firstName,
                it.middleName,
                it.email
            )
        }
    }

    fun findUserById(userId: Int): UserDataFull? = transaction {
        UserRow.findById(userId)?.run {
            UserDataFull(
                id.value,
                login,
                password,
                firstName,
                lastName,
                middleName,
                email
            )
        }
    }

    fun findUserByParameters(offset: Int, limit: Int, parameters: UserDataSearch): List<UserData> = transaction {
        val query = UsersTable.selectAll()

        parameters.firstName?.let { query.andWhere { UsersTable.firstName.lowerCase() like "%${it.decapitalize()}%" } }
        parameters.lastName?.let { query.andWhere { UsersTable.firstName.lowerCase() like "%${it.decapitalize()}%" } }
        parameters.middleName?.let { query.andWhere { UsersTable.firstName.lowerCase() like "%${it.decapitalize()}%" } }
        parameters.email?.let { query.andWhere { UsersTable.firstName.lowerCase() like "%${it.decapitalize()}%" } }

        return@transaction query.limit(limit, offset).map {
            UserData(
                it[UsersTable.id].value,
                it[UsersTable.email],
                it[UsersTable.lastName],
                it[UsersTable.firstName],
                it[UsersTable.middleName]
            )
        }
    }


    fun createProject(userId: Int, projectData: ProjectDataCreate): ProjectDataFull = transaction {
        ProjectRow.new {
            title = projectData.title.trimAllSpaces()
            created = userId
            documents = projectData.documents.writeValueAsString().trimAllSpaces()
            credentials = projectData.credentials.writeValueAsString().trimAllSpaces()
            description = projectData.description.trimAllSpaces()
            specification = projectData.specification.trimAllSpaces()
        }.run {
            ProjectDataFull(
                id.value,
                title,
                description,
                specification,
                json.readValue(documents),
                UserRow.find { UsersTable.id inList json.readValue<List<Int>>(credentials) }.map { user ->
                    UserData(
                        user.id.value,
                        user.email,
                        user.lastName,
                        user.firstName,
                        user.middleName
                    )
                },
                created,
                TaskRow.find { TasksTable.project eq id.value }.map { task ->
                    TaskDataFull(
                        task.id.value,
                        task.project,
                        task.title,
                        task.status,
                        task.created,
                        task.assigned,
                        task.description,
                        task.time.millis,
                        json.readValue(task.documents),
                        json.readValue(task.history)
                    )
                }
            )
        }
    }

    fun editProject(projectId: Int, newData: ProjectDataEdit): ProjectDataFull? = transaction {
        ProjectRow.findById(projectId)?.apply {
            newData.title?.let { title = it.trimAllSpaces() }
            newData.description?.let { description = it.trimAllSpaces() }
            newData.specification?.let { specification = it.trimAllSpaces() }
            newData.documents?.let { documents = it.writeValueAsString().trimAllSpaces() }
            newData.credentials?.let { credentials = it.writeValueAsString().trimAllSpaces() }
        }?.run {
            ProjectDataFull(
                id.value,
                title,
                description,
                specification,
                json.readValue(documents),
                UserRow.find { UsersTable.id inList json.readValue<List<Int>>(credentials) }.map { user ->
                    UserData(
                        user.id.value,
                        user.email,
                        user.lastName,
                        user.firstName,
                        user.middleName
                    )
                },
                created,
                TaskRow.find { TasksTable.project eq id.value }.map { task ->
                    TaskDataFull(
                        task.id.value,
                        task.project,
                        task.title,
                        task.status,
                        task.created,
                        task.assigned,
                        task.description,
                        task.time.millis,
                        json.readValue(task.documents),
                        json.readValue(task.history)
                    )
                }
            )
        }
    }

    fun canEditProject(userId: Int, projectId: Int): Boolean = transaction {
        val project = ProjectRow.findById(projectId)

        return@transaction if (project != null) {
            userId in json.readValue<List<Int>>(project.credentials) || userId == project.created
        } else false
    }

    fun findProjectByParameters(offset: Int, limit: Int, parameters: ProjectDataSearch, userId: Int): List<ProjectDataFull> = transaction {
        val query = ProjectsTable.selectAll()

        parameters.id?.let {query.andWhere { ProjectsTable.id eq it } }
        parameters.title?.let { query.andWhere { ProjectsTable.title.lowerCase() like "%${it.decapitalize()}%" } }
        parameters.description?.let { query.andWhere { ProjectsTable.description.lowerCase() like "%${it.decapitalize()}%" } }

        return@transaction query.limit(limit, offset).filter {
            userId in json.readValue<List<Int>>(it[ProjectsTable.credentials]) || userId == it[ProjectsTable.created]
        }.map {
            ProjectDataFull(
                it[ProjectsTable.id].value,
                it[ProjectsTable.title],
                it[ProjectsTable.description],
                it[ProjectsTable.specification],
                json.readValue(it[ProjectsTable.documents]),
                UserRow.find { UsersTable.id inList json.readValue<List<Int>>(it[ProjectsTable.credentials]) }.map { user ->
                    UserData(
                        user.id.value,
                        user.email,
                        user.lastName,
                        user.firstName,
                        user.middleName
                    )
                },
                it[ProjectsTable.created],
                TaskRow.find { TasksTable.project eq it[ProjectsTable.id].value }.map { task ->
                    TaskDataFull(
                        task.id.value,
                        task.project,
                        task.title,
                        task.status,
                        task.created,
                        task.assigned,
                        task.description,
                        task.time.millis,
                        json.readValue(task.documents),
                        json.readValue(task.history)
                    )
                }
            )
        }
    }


    fun createTask(userId: Int, taskData: TaskDataCreate): TaskDataFull = transaction {
        TaskRow.new {
            project = taskData.project
            title = taskData.title.trimAllSpaces()
            created = userId
            assigned = taskData.assigned
            description = taskData.description
            documents = taskData.documents.writeValueAsString()
        }.run {
            TaskDataFull(
                id.value,
                project,
                title,
                status,
                created,
                assigned,
                description,
                time.millis,
                json.readValue(documents),
                json.readValue(history)
            )
        }
    }

    fun editTask(taskId: Int, newData: TaskDataEdit): TaskDataFull? = transaction {
        TaskRow.findById(taskId)?.apply {
            newData.title?.let { title = it }
            newData.assigned?.let { assigned = it }
            newData.description?.let { description = it }
            newData.documents?.let { documents = it.writeValueAsString() }
        }?.run {
            TaskDataFull(
                id.value,
                project,
                title,
                status,
                created,
                assigned,
                description,
                time.millis,
                json.readValue(documents),
                json.readValue(history)
            )
        }
    }

    fun canEditTask(userId: Int, taskId: Int): Boolean = transaction {
        val task = TaskRow.findById(taskId)

        return@transaction if (task != null) {
            userId == task.created
        } else false
    }

    fun editTaskStatus(taskId: Int, userId: Int, comment: String, newStatus: Int): TaskDataFull? = transaction {
        TaskRow.findById(taskId)?.apply {
            history = (json.readValue<MutableList<HistoryItem>>(history) + HistoryItem(
                userId,
                status,
                newStatus,
                DateTime.now().millis,
                comment
            )).writeValueAsString()
        }?.run {
            TaskDataFull(
                id.value,
                project,
                title,
                status,
                created,
                assigned,
                description,
                time.millis,
                json.readValue(documents),
                json.readValue(history)
            )
        }
    }
}
