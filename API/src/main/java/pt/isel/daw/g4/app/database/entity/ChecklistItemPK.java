package pt.isel.daw.g4.app.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChecklistItemPK implements Serializable {

    @ManyToOne
    @JoinColumns(foreignKey = @ForeignKey(name = "FK_ChecklistItem_Checklist"), value = {
        @JoinColumn(referencedColumnName = "id", name = "checklist_id"),
        @JoinColumn(referencedColumnName = "user_id", name = "user_id")
    })
    public ChecklistEntity checklist_id;

    @Column(name = "id")
    public Long item_id;

    public ChecklistItemPK() { }

    public ChecklistItemPK(ChecklistEntity checklistId, Long item_id) {
        this.checklist_id = checklistId;
        this.item_id = item_id;
    }

    public ChecklistItemPK(ChecklistEntity number) {
        this.checklist_id = number;
    }

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistItemPK){
            ChecklistItemPK other = (ChecklistItemPK)obj;
            result = (
                    Objects.equals(this.checklist_id.getPk(), other.checklist_id.getPk()) &&
                    this.checklist_id.getDateToCompletion() == other.checklist_id.getDateToCompletion() &&
                    Objects.equals(this.checklist_id.getName(), other.checklist_id.getName()) &&
                    Objects.equals(this.checklist_id.getDescription(), other.checklist_id.getDescription()) &&
                    Objects.equals(this.checklist_id.getStatus(), other.checklist_id.getStatus()) &&
                    Objects.equals(this.item_id, other.item_id)
            );
        }
        return result;
    }

    @Override
    public int hashCode(){
        return (
                this.item_id.hashCode() * 41 +
                this.checklist_id.getPk().hashCode() * 17 +
                this.checklist_id.getName().hashCode() * 41 +
                this.checklist_id.getStatus().hashCode() * 17 +
                this.checklist_id.getDescription().hashCode() * 41 +
                this.checklist_id.getDateToCompletion().hashCode() * 17
        );
    }
}
