package util;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CSVFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getHeader() {
        return "id,type,name,status,description,epic,duration,startTime";
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription());

        if (task.getTaskType() == TaskType.SUBTASK) {
            sb.append(",").append(((Subtask) task).getEpic().getId());
        } else {
            sb.append(",");
        }

        sb.append(",").append(task.getDuration().toMinutes());
        sb.append(",").append(task.getStartTime() != null ? task.getStartTime().format(DATE_TIME_FORMATTER) : "");

        return sb.toString();
    }

    public static Task fromString(String csvLine, Map<Integer, Epic> epicsMap) {
        var fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[6]));
        LocalDateTime startTime = fields[7].isEmpty() ? null : LocalDateTime.parse(fields[7], DATE_TIME_FORMATTER);

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, duration, startTime);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                Epic parentEpic = epicsMap.get(epicId);
                if (parentEpic == null) {
                    throw new IllegalArgumentException("Эпик с ID " + epicId + " не найден для подзадачи с ID " + id);
                }
                return new Subtask(id, name, description, status, duration, startTime, parentEpic);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}