package pt.isel.daw.g4.app.model;

public class ChecklistProperties {
    private String name;
    private String description;
    private String dateToCompletion;
    private String status;

    public ChecklistProperties(String name, String description, String dateToCompletion, String status) {
        this.name = name;
        this.description = description;
        this.dateToCompletion = dateToCompletion;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDateToCompletion() {
        return dateToCompletion;
    }

    public String getStatus() {
        return status;
    }
}
