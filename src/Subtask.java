

public class Subtask extends Task {
    private Epic epic;

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

/*    public Subtask(Integer id, String name, String description) {
        super(name, description);
    }*/


    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }
}

