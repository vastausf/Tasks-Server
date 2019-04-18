data class TaskNewI(
    val data: TaskDataCreate
)

data class TaskNewO(
    val data: TaskDataFull
)

data class TaskEditI(
    val id: Int,
    val newData: TaskDataEdit
)

data class TaskEditO(
    val newData: TaskDataFull
)

data class TaskStatusEditI(
    val id: Int,
    val newStatus: Int,
    val comment: String
)

data class TaskStatusEditO(
    val data: TaskDataFull
)


data class TaskData(
    val id: Int,
    val project: Int,
    val title: String,
    val status: Int,
    val created: Int,
    val assigned: Int,
    val description: String,
    val documents: List<String>
)

data class TaskDataCreate(
    val project: Int,
    val title: String,
    val assigned: Int,
    val description: String,
    val documents: List<String>
)

data class TaskDataFull(
    val id: Int,
    val project: Int,
    val title: String,
    val status: Int,
    val created: Int,
    val assigned: Int,
    val description: String,
    val time: Long,
    val documents: List<String>,
    val history: List<HistoryItem>
)

data class TaskDataEdit(
    val title: String? = null,
    val assigned: Int? = null,
    val description: String? = null,
    val documents: List<String>? = null
)

data class HistoryItem(
    val userId: Int,
    val statusFrom: Int,
    val statusTo: Int,
    val time: Long,
    val comment: String
)
