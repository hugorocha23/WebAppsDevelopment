package pt.isel.daw.g4.app.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChecklistTemplateItemPK implements Serializable {

    @ManyToOne
    @JoinColumns(foreignKey = @ForeignKey(name = "FK_ChecklistItem_Checklist_Template"), value = {
            @JoinColumn(referencedColumnName = "user_id", name = "user_id"),
            @JoinColumn(referencedColumnName = "id", name = "template_id")
    })
    public ChecklistTemplateEntity checklist_template_id;

    @Column(name = "id")
    public Long id;

    public ChecklistTemplateItemPK() { }

    public ChecklistTemplateItemPK(ChecklistTemplateEntity checklistId, Long number) {
        this.checklist_template_id = checklistId;
        this.id = number;
    }

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistTemplateItemPK){
            ChecklistTemplateItemPK other = (ChecklistTemplateItemPK)obj;
            result = (
                    Objects.equals(this.checklist_template_id.getPk(), other.checklist_template_id.getPk()) &&
                            Objects.equals(this.checklist_template_id.getName(), other.checklist_template_id.getName()) &&
                            Objects.equals(this.checklist_template_id.getDescription(), other.checklist_template_id.getDescription()) &&
                            Objects.equals(this.id, other.id)
            );
        }
        return result;
    }

    @Override
    public int hashCode(){
        return (
                this.id.hashCode() * 41 +
                this.checklist_template_id.getPk().hashCode() * 17 +
                this.checklist_template_id.getName().hashCode() * 41 +
                this.checklist_template_id.getDescription().hashCode() * 41
        );
    }
}
