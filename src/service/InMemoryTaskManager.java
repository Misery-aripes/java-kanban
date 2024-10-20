package service;

import exeption.NotFoundException;
import history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));

    @Override
    public Task createTask(Task task) {
        task.setId(Task.getNextId());
        addToPrioritizedTasks(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        Task existingTask = tasks.get(task.getId());
        if (existingTask == null) {
            throw new NotFoundException("Задача с ID " + task.getId() + " не найдена.");
        }
        removeFromPrioritizedTasks(existingTask);
        addToPrioritizedTasks(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTaskById(int taskId) {
        Task task = tasks.remove(taskId);
        if (task == null) {
            throw new NotFoundException("Задача с ID " + taskId + " не найдена.");
        }
        removeFromPrioritizedTasks(task);
        historyManager.remove(taskId);
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NotFoundException("Задача с ID " + taskId + " не найдена.");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(Task.getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer taskId = epic.getId();
        if (taskId == null || !epics.containsKey(taskId)) {
            throw new NotFoundException("Эпик с ID " + taskId + " не найден.");
        }
        epics.put(taskId, epic);
        return epic;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic == null) {
            throw new NotFoundException("Эпик с ID " + epicId + " не найден.");
        }

        epic.getSubtaskList().stream()
                .map(Subtask::getId)
                .forEach(subtaskId -> {
                    Subtask subtask = subtasks.remove(subtaskId);
                    if (subtask != null) {
                        removeFromPrioritizedTasks(subtask);
                        historyManager.remove(subtaskId);
                    }
                });

        historyManager.remove(epicId);
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new NotFoundException("Эпик с ID " + epicId + " не найден.");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(Task.getNextId());
        addToPrioritizedTasks(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic().getId());
        if (epic == null) {
            throw new NotFoundException("Эпик с ID " + subtask.getEpic().getId() + " не найден.");
        }
        epic.getSubtaskList().add(subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask existingSubtask = subtasks.get(subtask.getId());
        if (existingSubtask == null) {
            throw new NotFoundException("Подзадача с ID " + subtask.getId() + " не найдена.");
        }
        removeFromPrioritizedTasks(existingSubtask);
        addToPrioritizedTasks(subtask);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic().getId());
        if (epic == null) {
            throw new NotFoundException("Эпик с ID " + subtask.getEpic().getId() + " не найден.");
        }
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с ID " + subtaskId + " не найдена.");
        }
        removeFromPrioritizedTasks(subtask);

        Epic epic = subtask.getEpic();
        if (epic != null) {
            epic.getSubtaskList().removeIf(s -> s.getId() == subtaskId);
            updateEpicStatus(epic);
        }
        historyManager.remove(subtaskId);
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с ID " + subtaskId + " не найдена.");
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void deleteAllEpics() {
        Set<Integer> epicIds = new HashSet<>(epics.keySet());
        epics.clear();
        subtasks.clear();
        historyManager.getHistory().removeIf(task -> epicIds.contains(task.getId()));
    }

    @Override
    public void deleteAllSubtasks() {
        Set<Integer> subtaskIds = new HashSet<>(subtasks.keySet());
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.getSubtaskList().clear();
            updateEpicStatus(epic);
        });

        historyManager.getHistory().removeIf(task -> subtaskIds.contains(task.getId()));
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

    protected void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtaskList();

        if (subtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = subtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.NEW);
        boolean allDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);

        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public boolean isTimeOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }

        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }

    private void addToPrioritizedTasks(Task newTask) {
        boolean isOverlapping = prioritizedTasks.stream()
                .anyMatch(existingTask -> isTimeOverlap(existingTask, newTask));

        if (isOverlapping) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }
        prioritizedTasks.add(newTask);
    }

    void removeFromPrioritizedTasks(Task task) {
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}