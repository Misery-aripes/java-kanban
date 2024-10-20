package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtaskList;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Duration.ZERO, null);
        this.subtaskList = new ArrayList<>();
        calculateEpicFields();
    }

    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status, Duration.ZERO, null);
        this.subtaskList = new ArrayList<>();
        calculateEpicFields();
    }

    private void calculateEpicFields() {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;

        for (Subtask subtask : subtaskList) {
            duration = duration.plus(subtask.getDuration());

            if (startTime == null || (subtask.getStartTime() != null && subtask.getStartTime().isBefore(startTime))) {
                startTime = subtask.getStartTime();
            }

            LocalDateTime subtaskEndTime = subtask.getEndTime();
            if (endTime == null || (subtaskEndTime != null && subtaskEndTime.isAfter(endTime))) {
                endTime = subtaskEndTime;
            }
        }
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        calculateEpicFields();
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }
}