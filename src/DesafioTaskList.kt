import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import kotlin.Boolean

// Utilize companion object para gerar IDs únicos automaticamente para cada Task.
@ConsistentCopyVisibility
data class Task private constructor(
    val id: Int,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val createdAt: String
) {
    companion object {
        private val idGenerator = AtomicInteger(0)

        fun create(
            title: String,
            description: String?,
            isCompleted: Boolean
        ): Task {
            return Task(
                id = idGenerator.incrementAndGet(),
                title = title,
                description = description,
                isCompleted = isCompleted,
                createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            )
        }

        fun switchStatus(
            task: Task
        ): Task {
            return Task(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = !task.isCompleted,
                createdAt = task.createdAt
            )
        }
    }
}

interface Manager<Task>{
    fun addTask(task: Task)
    fun listTask(): List<String>
    fun searchTask(id: Int): Task?
    fun updateTask(task: Task): Boolean
    fun deleteTask(id: Int): Boolean
    fun filterTask(isCompleted: Boolean): List<Task>
}

class TaskManager: Manager<Task> {
    private val taskList = mutableListOf<Task>()

    override fun addTask(task: Task) {
        taskList.add(task)
    }

    override fun listTask(): List<String>{
        return formatTask(taskList)
    }

    override fun searchTask(id: Int): Task? {
        return taskList.find { it.id == id }
    }

    override fun updateTask(task: Task): Boolean {
        val updatedTask = Task.switchStatus(task)

        if (taskList.removeIf { it.id == task.id }){
            taskList.add(updatedTask)
            return true
        } else
            return false
    }

    override fun deleteTask(id: Int): Boolean {
        return taskList.removeIf { it.id == id }
    }

    override fun filterTask(isCompleted: Boolean): List<Task> {
        // se eu entrar com true ou false,
        return taskList.filter { it.isCompleted == isCompleted }
    }
}

fun formatTask(task: List<Task>): List<String> {
    val taskListDestructured = task.map { (id, title, description, isCompleted) ->
        "Tarefa $id | " +
                "Título: $title | " +
//           "Descrição: ${ if (description!!.isEmpty()) "Sem descrição" else description }" +
                "Status: ${if (isCompleted) "Concluída" else "Não concluída"}" }

    return taskListDestructured
}

fun fillTask(): Task{
    var title: String? = null
    println("Insira o título da tarefa:")
    print("-> ")
    while (title.isNullOrEmpty()){
        title = readlnOrNull()
        if (title.isNullOrEmpty()) println("Título inválido. Insira novamente.")
    }

    var description: String? = null
    println("Insira a descrição da tarefa:")
    print("-> ")
    while (description == null){
        description = readlnOrNull()
        if (description == null) println("Descrição inválida. Insira novamente")
    }

    var isCompletedBoolean: Boolean? = null
    println("Insira o status da tarefa (1 para concluído e 2 para não concluído):")
    print("-> ")
    while (isCompletedBoolean == null){
        val input = readlnOrNull()
        isCompletedBoolean = when (input) {
            "1" -> true
            "2" -> false
            else -> {
                println("Status inválido. Insira novamente.")
                null
            }
        }
    }
    return Task.create(title, description, isCompletedBoolean)
}


fun main(){
    val taskManager = TaskManager()
    var acao: Int? = null

    while (acao != 7){
        println()
        println("""TASKMANAGER
            |1. Adicionar uma nova tarefa
            |2. Listar todas as tarefas
            |3. Buscar tarefa por ID
            |4. Atualizar status - em andamento (aplicar melhorias)
            |5. Excluir tarefa por ID - pendente
            |6. Filtrar tarefas concluídas ou pendentes
            |7. Sair"""
            .trimMargin())
        print("Insira a opção desejada: ")
        acao = readlnOrNull()?.toIntOrNull()

        when(acao) {
            1 -> {
                val task = fillTask()
                taskManager.addTask(task)
            }

            2 -> {
                println(taskManager.listTask().joinToString("\n").ifEmpty { "Nenhuma tarefa cadastrada" })
            }

            3 -> {
                var id: Int? = null
                println("Insira o ID da tarefa a ser buscada:")
                print("-> ")
                while (id == null || id < 0){
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id < 0 || taskManager.searchTask(id) == null) {
                        println("O ID inserido é inválido. Tente novamente")
                    }
                }

                val taskFound = taskManager.searchTask(id)
                taskFound?.let {
                    println(
                        "O produto buscado é: $it"
                    )
                } ?: "Não há tarefas com este ID"
            }

            // desenvolver melhorias:
            // printar um antes e depois e não todas as tasks

            4 -> {
                var id: Int? = null
                println("Insira o ID da tarefa a ser buscada:")
                print("-> ")
                while (id == null || id < 0){
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id < 0 || taskManager.searchTask(id) == null) {
                        println("O ID inserido é inválido. Tente novamente")
                    }
                }

                val taskFound = taskManager.searchTask(id)
                taskFound?.let {
                    taskManager.updateTask(taskFound)
                    println("Tarefa atualizada com sucesso!")
                    println(taskManager.listTask().joinToString(separator = "\n"))
                } ?: "Não há tarefa com este ID"

            }

            6 -> {
                var isCompletedBoolean: Boolean? = null
                println("Insira 1 ou 2 para filtrar as tarefas (1 para concluído e 2 para não concluído):")
                print("-> ")
                while (isCompletedBoolean == null){
                    val input = readlnOrNull()
                    isCompletedBoolean = when (input) {
                        "1" -> {
                            true
                        }
                        "2" -> false
                        else -> {
                            println("Status inválido. Insira novamente.")
                            null
                        }
                    }
                }
                val filteredTasks = taskManager.filterTask(isCompletedBoolean)
                println(formatTask(filteredTasks).joinToString(separator = "\n").ifEmpty { "Nenhuma tarefa cadastrada" })
            }
            else -> "Teste"
        }
    }
}