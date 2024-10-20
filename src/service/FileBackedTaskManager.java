package service;

import exeption.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import util.CSVFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            List<Integer> idTask = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                Task task = CSVFormatter.fromString(lines.get(i), fileBackedTaskManager.getEpicsMap());
                idTask.add(task.getId());
                if (task instanceof Epic) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.addSubtask((Subtask) task);
                } else {
                    fileBackedTaskManager.addTask(task);
                }
            }
            if (!idTask.isEmpty()) {
                int maxID = Collections.max(idTask);
                Task.setNextId(maxID + 1);
            }
        } catch (IOException e) {
            throw ManagerSaveException.loadException(e);
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            for (Task task : getTasks()) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }
            for (Epic epic : getEpics()) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        save();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();
        return updatedEpic;
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic().getId());
        epic.getSubtaskList().add(subtask);
    }

    public Map<Integer, Epic> getEpicsMap() {
        return epics;
    }
}