import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private static int nextId = 1;

    private int getNextId() {
        return nextId++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(getNextId());
        this.tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !this.tasks.containsKey(taskId)) {
            return null;
        }
        this.tasks.put(taskId, task);
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        this.epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer taskId = epic.getId();
        if (taskId == null || !this.epics.containsKey(taskId)) {
            return null;
        }
        this.epics.put(taskId, epic);
        return epic;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtaskList()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            historyManager.remove(epicId);
        }
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
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

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.getSubtaskList().remove(subtask);
                updateEpicStatus(epic);
            }
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void deleteAllEpics() {
        Set<Integer> epicId = new HashSet<>(epics.keySet());
        epics.clear();
        subtasks.clear();
        historyManager.getHistory().removeIf(task -> epicId.contains(task.getId()));
    }

    @Override
    public void deleteAllSubtasks() {
        Set<Integer> subtuskId = new HashSet<>(subtasks.keySet());
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskList().clear();
            updateEpicStatus(epic);
        }
        historyManager.getHistory().removeIf(task -> subtuskId.contains(task.getId()));
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        historyManager.getHistory().clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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
}