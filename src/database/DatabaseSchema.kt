import org.jetbrains.exposed.dao.*
import org.joda.time.DateTime

object UsersTable: IntIdTable() {
    val email = varchar("email", 64)
    val login = varchar("login", 32)
    val password = varchar("password", 64)
    val lastName = varchar("last_name", 32)
    val firstName = varchar("first_name", 32)
    val middleName = varchar("middle_name", 32)
}

class UserRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserRow>(UsersTable)

    var email by UsersTable.email
    var login by UsersTable.login
    var password by UsersTable.password
    var lastName by UsersTable.lastName
    var firstName by UsersTable.firstName
    var middleName by UsersTable.middleName
}

object ProjectsTable : IntIdTable() {
    val title = varchar("title", 64)
    val created = integer("created")
    val documents = text("documents")
    val credentials = text("credentials")
    val description = text("description")
    val specification = text("specification")
}

class ProjectRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ProjectRow>(ProjectsTable)

    var title by ProjectsTable.title
    var created by ProjectsTable.created
    var documents by ProjectsTable.documents
    var description by ProjectsTable.description
    var credentials by ProjectsTable.credentials
    var specification by ProjectsTable.specification
}

object TasksTable : IntIdTable() {
    val time = datetime("time").default(DateTime.now())
    val title = varchar("title", 64)
    val status = integer("status").default(0)
    val history = text("history").default(emptyList<HistoryItem>().writeValueAsString())
    val project = integer("project")
    val created = integer("created")
    val assigned = integer("assigned")
    val documents = text("documents")
    val description = text("description")
}

class TaskRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TaskRow>(TasksTable)

    var time by TasksTable.time
    var title by TasksTable.title
    var status by TasksTable.status
    var history by TasksTable.history
    var project by TasksTable.project
    var created by TasksTable.created
    var assigned by TasksTable.assigned
    var documents by TasksTable.documents
    var description by TasksTable.description
}