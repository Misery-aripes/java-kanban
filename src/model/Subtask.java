package model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Subtask(int id, String name, String description, TaskStatus status, Epic epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    public Subtask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Subtask(String name, String description, Duration duration, LocalDateTime startTime, Epic epic) {
        super(name, description, duration, startTime);
        this.epic = epic;
    }

    public Subtask(String name, String description, TaskStatus status, Duration duration,
                   LocalDateTime startTime, Epic epic) {
        super(name, description, status, duration, startTime);
        this.epic = epic;  // Сохраняем объект Epic
    }

    public Subtask(int id, String name, String description, TaskStatus status, Duration duration,
                   LocalDateTime startTime, Epic epic) {
        super(id, name, description, status, duration, startTime);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public int getEpicId() {
        return epic.getId();
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }
}