package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    Task createTask(Task task);

    Task updateTask(Task task);

    List<Task> getTasks();

    void deleteTaskById(int taskId);

    Task getTask(int taskId);

    Epic createEpic(Epic epic);

    Epic updateEpic(Epic epic);

    List<Epic> getEpics();

    void deleteEpicById(int epicId);

    Epic getEpicById(int epicId);

    Subtask createSubtask(Subtask subtask);

    List<Subtask> getSubtasks();

    void deleteSubtaskById(int subtaskId);

    Subtask getSubtask(int subtaskId);

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteAllTasks();

    List<Task> getHistory();

    Subtask updateSubtask(Subtask subtask);

    List<Task> getPrioritizedTasks();
}