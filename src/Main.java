import java.util.List;

public class Main {

    public static void main(String[] args) {
        testTask();
    }

    private static void testTask() {
        TaskKeeper taskKeeper = new TaskKeeper();

        List<Task> taskMap = taskKeeper.getTasks();
        System.out.println("Таски должны быть пустые: " + taskMap.isEmpty());

        Task task1 = new Task("Имя первой тасик", "Описание первой таски");
        Task taskCreated = taskKeeper.createTask(task1);
        System.out.println("Таска должна содержать id: " + (taskCreated.getId() != null));
        System.out.println("Список тасок должен содержать созданную таску: " + taskKeeper.getTasks());
        System.out.println();

        Task updateTask1 = new Task(taskCreated.getId(), "Новое имя первой таски",
                "Новое описание второй таски", TaskStatus.IN_PROGRESS);
        Task task2Updated = taskKeeper.updateTask(updateTask1);
        System.out.println("Обновленная таска должна содержать обновленные данные: " + task2Updated);
        System.out.println();

        Task newTask2 = new Task("Имя второй задачи", "Описание второй задачи");
        Task newTaskCreated = taskKeeper.createTask(newTask2);
        System.out.println("Создание новой таски: " + newTaskCreated);
        System.out.println();

        boolean deleteTask = taskKeeper.deleteTaskById(task2Updated.getId());
        System.out.println("Проверка удаления такски: " + deleteTask);
        System.out.println("Список : " + taskKeeper.getTasks());
        System.out.println();

        Epic epicTask = new Epic("Имя епика", "Описание эпика");
        Task epicCreated = taskKeeper.createEpic(epicTask);
        System.out.println("Эпик должен содержать id: " + (taskCreated.getId() != null));
        System.out.println("Список эпиков должен содержать созданный эпик: " + taskKeeper.getEpic());
        System.out.println();

        Epic updateEpic = new Epic(epicCreated.getId(), "Новое имя первого эпика",
                "Новое описание второго эпика", TaskStatus.IN_PROGRESS);
        Task epicUpdated = taskKeeper.updateEpic(updateEpic);
        System.out.println("Обновленный эпик должен содержать обновленные данные: " + epicUpdated);
        System.out.println();

        System.out.println("Проверка добавления и удаления сабтаски из эпика: ");
        Epic createdEpic = taskKeeper.createEpic(new Epic("Имя эпика", "Описание эпика"));
        Subtask createdSub = taskKeeper.createSubtask(new Subtask("Имя сабтаски", "Описание сабтаски"));
        Subtask addEpic = taskKeeper.addEpicToSubtask(createdEpic.getId(), createdSub.getId());
        Epic addDub = taskKeeper.addSubtaskToEpic(addEpic.getId(), createdEpic.getId());
        taskKeeper.deleteSubTaskFromEpic(createdEpic.getId(), createdSub.getId());
        taskKeeper.deleteEpicFromSubTask(createdSub.getId());
        var deletedSub = taskKeeper.deleteSubTaskById(addEpic.getId());
        System.out.println(deletedSub + "," + addDub);



    }
}
