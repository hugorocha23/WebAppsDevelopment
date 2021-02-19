package pt.isel.daw.g4.app.model;

public class ChecklistTemplateItemProperties {

    private Long id;

    private Long checklist_template_id;

    private String name;

    private String description;

    public ChecklistTemplateItemProperties(Long id, Long checklist_template_id, String name, String description) {
        this.id = id;
        this.checklist_template_id = checklist_template_id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getChecklist_template_id() {
        return checklist_template_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
