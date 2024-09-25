package util;

import model.*;

import java.util.Map;

public class CSVFormatter {

    private CSVFormatter() {

    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription());
        if (task.getTaskType().equals(TaskType.SUBTASK)) {
            sb.append(",").append(((Subtask) task).getEpic().getId());
        }
        return sb.toString();
    }

    public static Task fromString(String csvLine, Map<Integer, Epic> epicsMap) {
        var fields = csvLine.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                Epic parentEpic = epicsMap.get(epicId);
                if (parentEpic == null) {
                    throw new IllegalArgumentException("Эпик с ID " + epicId + " не найден для подзадачи с ID " + id);
                }
                return new Subtask(id, name, description, status, parentEpic);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}