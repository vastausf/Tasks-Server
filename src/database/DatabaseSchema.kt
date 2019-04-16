import org.jetbrains.exposed.dao.*

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
    val documents = text("documents")
    val credentials = text("credentials")
    val description = text("description")
    val specification = text("specification")
}

class ProjectRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ProjectRow>(ProjectsTable)

    var title by ProjectsTable.title
    var documents by ProjectsTable.documents
    var description by ProjectsTable.description
    var credentials by ProjectsTable.credentials
    var specification by ProjectsTable.specification
}

object TasksTable : IntIdTable() {
    val title = varchar("title", 64)
    val documents = text("documents")
    val description = text("description")
}

class TaskRow(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TaskRow>(TasksTable)

    var title by TasksTable.title
    var documents by TasksTable.documents
    var description by TasksTable.description
}