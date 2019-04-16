import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import module.auth.UserDataFull
import module.project.ProjectData
import module.project.ProjectDataFull
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseClient {
    init {
        Database.connect(
            "jdbc:h2:tcp:194.1.239.114:9092/./root/tasks_server/database",
            "org.h2.Driver",
            "tasks_server",
            "tasks2043"
        )

        transaction {
            SchemaUtils.create(UsersTable, ProjectsTable, TasksTable)
        }

        //createUsers()
    }

    val json = jacksonObjectMapper()

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
                    this.firstName = firstName
                    this.lastName = lastName
                    this.middleName = middleName
                    this.email = "$firstName.$lastName@mail.com"
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


    fun createProject(projectData: ProjectData): ProjectDataFull = transaction {
        ProjectRow.new {
            title = projectData.title
            documents = projectData.documents.writeValueAsString()
            credentials = projectData.credentials.writeValueAsString()
            description = projectData.description
            specification = projectData.specification
        }.run {
            ProjectDataFull(
                id.value,
                title,
                description,
                specification,
                json.readValue(credentials),
                json.readValue(documents)
            )
        }
    }
}