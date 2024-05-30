import java.util.List;

public class Main {

    public static void main(String[] args) {
        testTask();
    }

    private static void testTask() {
        TaskManager taskManager = new TaskManager();

        List<Task> tasks = taskManager.getTasks();
        System.out.println("Таски должны быть пустые: " + tasks.isEmpty());
        System.out.println();

        Task task1 = new Task("Имя первой таски", "Описание первой таски");
        Task taskCreated = taskManager.createTask(task1);
        System.out.println("Таска должна содержать id: " + (taskCreated.getId() != null));
        System.out.println("Список тасок должен содержать созданную таску: " + taskManager.getTasks());
        System.out.println();

        Task updateTask1 = new Task(taskCreated.getId(), "Новое имя первой таски",
                "Новое описание первой таски", TaskStatus.IN_PROGRESS);
        Task taskUpdated = taskManager.updateTask(updateTask1);
        System.out.println("Обновленная таска должна содержать обновленные данные: " + taskUpdated);
        System.out.println();

        Task newTask2 = new Task("Имя второй таски", "Описание второй таски");
        Task newTaskCreated = taskManager.createTask(newTask2);
        System.out.println("Создание новой таски: " + newTaskCreated);
        System.out.println();

        boolean deleteTask = taskManager.deleteTaskById(taskUpdated.getId());
        System.out.println("Проверка удаления таски: " + deleteTask);
        System.out.println("Список тасок: " + taskManager.getTasks());
        System.out.println();

        System.out.println("Список всех тасок перед удалением: " + taskManager.getTasks());

        List<Task> removedTasks = taskManager.deleteAllTasks();
        System.out.println("Удаленные таски: " + removedTasks);
        System.out.println("Список тасок после удаления: " + taskManager.getTasks());
        System.out.println();

        Epic epicTask = new Epic("Имя эпика", "Описание эпика");
        Epic epicCreated = taskManager.createEpic(epicTask);
        System.out.println("Эпик должен содержать id: " + (epicCreated.getId() != null));
        System.out.println("Список эпиков должен содержать созданный эпик: " + taskManager.getEpics());
        System.out.println();

        Epic epicTask2 = new Epic("Имя второго эпика", "Описание второго эпика");
        Epic epicCreated2 = taskManager.createEpic(epicTask2);
        System.out.println("Второй эпик должен содержать id: " + (epicCreated2.getId() != null));
        System.out.println("Список эпиков должен содержать созданный эпик: " + taskManager.getEpics());
        System.out.println();

        Epic updateEpic = new Epic(epicCreated.getId(), "Новое имя первого эпика",
                "Новое описание первого эпика", TaskStatus.IN_PROGRESS);
        Epic epicUpdated = taskManager.updateEpic(updateEpic);
        System.out.println("Обновленный эпик должен содержать обновленные данные: " + epicUpdated);
        System.out.println();

        boolean deleteEpic = taskManager.deleteEpicById(epicTask.getId());
        System.out.println("Проверка удаления эпика: " + deleteEpic);
        System.out.println("Список эпиков: " + taskManager.getEpics());
        System.out.println();

        System.out.println("Список всех эпиков перед удалением: " + taskManager.getEpics());

        List<Epic> removedEpic = taskManager.deleteAllEpics();
        System.out.println("Удаленные эпики: " + removedEpic);
        System.out.println("Список эпиков после удаления: " + taskManager.getEpics());
        System.out.println("");

        System.out.println("Проверка добавления и удаления сабтасок из эпика: ");
        Epic createdEpic = taskManager.createEpic(new Epic("Имя эпика", "Описание эпика"));
        Subtask createdSub = taskManager.createSubtask(new Subtask("Имя сабтаски", "Описание сабтаски",
                createdEpic));
        Subtask createdSub2 = taskManager.createSubtask(new Subtask("Имя второй сабтаски",
                "Описание второй сабтаски",
                createdEpic));
        System.out.println("Сабтаски добавлены к эпику: " + createdEpic.getSubtaskList());

        taskManager.deleteSubTaskById(createdSub.getId());
        System.out.println("Сабтаска удалена: " + createdEpic.getSubtaskList());
        System.out.println();

        taskManager.deleteAllSubtasks();
        System.out.println("Проверка удаления всех сабтасок: " + taskManager.getSubtasks());
        System.out.println();

        System.out.println("Проверка удаления всех тасков: " + taskManager.deleteAllTasks());
    }
}