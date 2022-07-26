public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, String description, String status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }
}
