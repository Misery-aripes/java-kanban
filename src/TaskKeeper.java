import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskKeeper {
    public Map<Integer, Task> taskMap = new HashMap<>();
    public Map<Integer, Subtask> subtaskMap = new HashMap<>();
    public Map<Integer, Epic> epicMap = new HashMap<>();

    private static int nextId = 1;

    public Task createTask(Task task) {
        task.setId(getNextId());
        taskMap.put(task.getId(), task);
        return task;
    }

    public int getNextId() {
        return nextId++;
    }

    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !taskMap.containsKey(taskId)) {
            return null;
        }
        taskMap.put(taskId, task);
        return task;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public List<Epic> getEpic() {
        return new ArrayList<>(epicMap.values());
    }

    public boolean deleteTaskById(int taskId) {
        return taskMap.remove(taskId) != null;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtaskMap.put(subtask.getId(), subtask);
        return subtask;
    }

    public Subtask addEpicToSubtask(int epicId, int subtaskId) {
        Epic epic = epicMap.get(epicId);
        Subtask subtask = subtaskMap.get(subtaskId);
        subtask.setEpic(epic);
        return subtask;
    }

    public void deleteSubTaskFromEpic(int epicId, int subtaskId) {
        Epic epic = epicMap.get(epicId);
        Subtask subtask = subtaskMap.get(subtaskId);
        epic.getSubtaskList().remove(subtask);
    }

    public void deleteEpicFromSubTask(int subtaskId) {
        Subtask subtask = subtaskMap.get(subtaskId);
        subtask.setEpic(null);
    }

    public Subtask deleteSubTaskById(int subtaskId) {
        return subtaskMap.remove(subtaskId);
    }

    public Epic addSubtaskToEpic(int subtaskId, int epicId) {
        Epic epic = epicMap.get(epicId);
        Subtask subtask = subtaskMap.get(subtaskId);
        epic.getSubtaskList().add(subtask);
        return epic;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epicMap.put(epic.getId(), epic);
        return epic;
    }

    public Epic Epic(Epic epic) {
        epicMap.put(epic.getId(), epic);
        return epic;
    }

    public Epic updateEpic (Epic epic) {
        Integer taskId = epic.getId();
        if (taskId == null || !epicMap.containsKey(taskId)) {
            return null;
        }
        epicMap.put(taskId, epic);
        return epic;
    }

    public void chekEpikSubtask(Subtask subtask) {
        boolean allDone = true;
        if (subtask.getStatus() == TaskStatus.DONE || subtask.getEpic() != null) {
            for (Subtask subtask1 : subtask.getEpic().getSubtaskList()) {
                if (subtask1.getStatus() != TaskStatus.DONE) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                Epic epic = subtask.getEpic();
                epic.setStatus(TaskStatus.DONE);
                epicMap.put(subtask.getEpic().getId(), epic);
            }
        }
    }
}





