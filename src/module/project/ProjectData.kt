package module.project

data class ProjectCreateI(
    val data: ProjectData
)

data class ProjectCreateO(
    val data: ProjectDataFull
)

data class ProjectData(
    val title: String,
    val description: String,
    val specification: String,
    val credentials: List<Int>,
    val documents: List<String>
)

data class ProjectDataFull(
    val id: Int,
    val title: String,
    val description: String,
    val specification: String,
    val credentials: List<Int>,
    val documents: List<String>
)
