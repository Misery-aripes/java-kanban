import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private static int nextId = 1;

    private int getNextId() {
        return nextId++;
    }

    public Task createTask(Task task) {
        task.setId(getNextId());
        this.tasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !this.tasks.containsKey(taskId)) {
            return null;
        }
        this.tasks.put(taskId, task);
        return task;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        this.epics.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        Integer taskId = epic.getId();
        if (taskId == null || !this.epics.containsKey(taskId)) {
            return null;
        }
        this.epics.put(taskId, epic);
        return epic;
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteEpicById(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic != null) {
        for (Subtask subtask : epic.getSubtaskList()) {
            subtasks.remove(subtask.getId());
        }
        }
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic().getId());
        if (epic != null) {
            epic.getSubtaskList().add(subtask);
            updateEpicStatus(epic);
        }
        return subtask;
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.getSubtaskList().remove(subtask);
                updateEpicStatus(epic);
            }
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        for (Subtask subtask : epic.getSubtaskList()) {
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
                break;
            }
        }

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
            updateEpicStatus(epic);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }
}





