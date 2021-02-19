package pt.isel.daw.g4.app.database.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;


@Entity(name = "ChecklistItem")
public class ChecklistItemEntity implements Serializable {

    @EmbeddedId
    private ChecklistItemPK pk;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    public ChecklistItemEntity(ChecklistItemPK checklist_id, String name, String description) {
        this.pk = checklist_id;
        this.name = name;
        this.status = "uncompleted";
        this.description = description;
    }
    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistItemEntity){
            ChecklistItemEntity other = (ChecklistItemEntity)obj;
            result = (
                    Objects.equals(this.pk.checklist_id.getPk().id, other.pk.checklist_id.getPk().id) &&
                    Objects.equals(this.pk.item_id, other.pk.item_id) &&
                    Objects.equals(this.description,other.getDescription())&&
                    Objects.equals(this.name, other.getName()) &&
                    Objects.equals(this.status, other.getStatus())
            );
        }
        return result;
    }

    public ChecklistItemEntity(){}

    public ChecklistItemPK getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setPk(ChecklistItemPK pk) {
        this.pk = pk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}