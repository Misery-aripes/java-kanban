import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtaskList;

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskList = new ArrayList<>();
    }

    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.subtaskList = new ArrayList<>();
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }
}