package pt.isel.daw.g4.app.database.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "ChecklistTemplate")
public class ChecklistTemplateEntity implements Serializable {

    @EmbeddedId
    private ChecklistTemplatePK pk;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public ChecklistTemplateEntity(ChecklistTemplatePK pk, String name, String description) {
        this.pk = pk;
        this.name = name;
        this.description = description;
    }

    public ChecklistTemplateEntity(){}

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistTemplateEntity){
            ChecklistTemplateEntity other = (ChecklistTemplateEntity)obj;
            result = (
                    Objects.equals(this.pk.id, other.pk.id) &&
                            Objects.equals(this.name, other.name) &&
                            Objects.equals(this.description, other.description)
            );
        }
        return result;
    }

    public void setPk(ChecklistTemplatePK pk) {
        this.pk = pk;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChecklistTemplatePK getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
