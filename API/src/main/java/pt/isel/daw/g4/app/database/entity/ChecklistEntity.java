package pt.isel.daw.g4.app.database.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "Checklist")
public class ChecklistEntity implements Serializable {

    @EmbeddedId
    private ChecklistPK pk;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "date_to_completion", nullable = false)
    private String dateToCompletion;

    public ChecklistEntity(ChecklistPK pk, String name, String description, String dateToCompletion ) {
        this.pk = pk;
        this.name = name;
        this.description = description;
        this.status = "uncompleted";
        this.dateToCompletion = dateToCompletion;
    }

    public ChecklistEntity() {}

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistEntity){
            ChecklistEntity other = (ChecklistEntity)obj;
            result = (
                    Objects.equals(this.pk.id, other.pk.id) &&
                    Objects.equals(this.name, other.name) &&
                    Objects.equals(this.description, other.description) &&
                    Objects.equals(this.status, other.status) &&
                    Objects.equals(this.dateToCompletion, other.dateToCompletion)
            );
        }
        return result;
    }

    public void setPk(ChecklistPK pk) {
        this.pk = pk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateToCompletion(String dateToCompletion) {
        this.dateToCompletion = dateToCompletion;
    }

    public ChecklistPK getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getDateToCompletion() {
        return dateToCompletion;
    }
}
