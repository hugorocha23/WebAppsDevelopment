package pt.isel.daw.g4.app.model;

public class ChecklistItemProperties {

    private Long id;

    private Long checklist_id;

    private String name;

    private String state;

    private String description;

    public ChecklistItemProperties(Long id, Long checklist_id, String name, String state, String description) {
        this.id = id;
        this.checklist_id = checklist_id;
        this.name = name;
        this.state = state;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getChecklist_id() {
        return checklist_id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}
