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
    val description = text("description")
    val specification = text("specification")
}

object ProjectCredentials: IntIdTable() {
    val project = reference("project", ProjectsTable)
    val user = reference("user", UsersTable)
}

class ProjectCredentialRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ProjectCredentialRow>(ProjectCredentials)

    var project by ProjectCredentialRow referencedOn ProjectCredentials.project
    var user by ProjectCredentialRow referencedOn ProjectCredentials.user
}

class ProjectRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ProjectRow>(ProjectsTable)

    var tasks by TaskRow referencedOn ProjectsTable.tasks
    var title by ProjectsTable.title
    var created by ProjectsTable.created
    var documents by ProjectsTable.documents
    var description by ProjectsTable.description
    var specification by ProjectsTable.specification
}

object TasksTable : IntIdTable() {
    val time = datetime("time").default(DateTime.now())
    val title = varchar("title", 64)
    val status = integer("status").default(0)
    val history = text("history").default(emptyList<HistoryItem>().writeValueAsString())
    val project = reference("project", ProjectsTable)
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
    var project by ProjectRow referencedOn TasksTable.project
    var created by TasksTable.created
    var assigned by TasksTable.assigned
    var documents by TasksTable.documents
    var description by TasksTable.description
}