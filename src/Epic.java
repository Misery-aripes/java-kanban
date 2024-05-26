import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskList = new ArrayList<>();
    }


    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }


}
