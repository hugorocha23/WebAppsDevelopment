package pt.isel.daw.g4.app.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChecklistTemplatePK implements Serializable{

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_ChecklistTemplate_Users"),
            name = "user_id", referencedColumnName = "id")
    public UserEntity user;

    @Column(name = "id")
    public Long id;

    public ChecklistTemplatePK() { }

    public ChecklistTemplatePK(UserEntity user, Long id) {
        this.user = user;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistTemplatePK){
            ChecklistTemplatePK other = (ChecklistTemplatePK)obj;
            result = (
                    Objects.equals(this.id, other.id) &&
                    Objects.equals(this.user.getUser_id(), other.user.getUser_id()) &&
                    Objects.equals(this.user.getPassword(), other.user.getPassword())
            );
        }
        return result;
    }

    @Override
    public int hashCode(){
        return (
                this.id.hashCode() * 41 +
                this.user.getUser_id().hashCode() * 17 +
                this.user.getPassword().hashCode() * 41
        );
    }

}
