package model;

import java.util.Objects;

public class Task {
    private static int nextId = 1;
    private Integer id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(String name, String description) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(Integer id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, TaskStatus status, Epic epic) {
    }

    public Task(String testTask, String testDescription, TaskStatus taskStatus) {
        this.name = testTask;
        this.description = testDescription;
        this.status = taskStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public static void resetNextId() {
        nextId = 1;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    public static int getNextId() {
        return nextId;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description,
                task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}