package pt.isel.daw.g4.app.database.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;


@Entity(name = "ChecklistTemplateItem")
public class ChecklistTemplateItemEntity {

    @EmbeddedId
    private ChecklistTemplateItemPK pk;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public ChecklistTemplateItemEntity(ChecklistTemplateItemPK checklistTemplate, String name, String description) {
        this.pk = checklistTemplate;
        this.name = name;
        this.description = description;
    }

    public ChecklistTemplateItemEntity(){

    }
    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistTemplateItemEntity){
            ChecklistTemplateItemEntity other = (ChecklistTemplateItemEntity)obj;
            result = (
                    Objects.equals(this.pk.checklist_template_id.getPk().id,other.pk.checklist_template_id.getPk().id) &&
                    Objects.equals(this.pk.id, other.getPk().id) &&
                    Objects.equals(this.name, other.getName()) &&
                    Objects.equals(this.description, other.getDescription())
            );
        }
        return result;
    }

    public void setPk(ChecklistTemplateItemPK pk) {
        this.pk = pk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChecklistTemplateItemPK getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}