package pt.isel.daw.g4.app.database.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChecklistPK implements Serializable {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_Checklist_Users"),
            name = "user_id",referencedColumnName = "id")
    public UserEntity user_id;

    @Column(name = "id")
    public Long id;

    public ChecklistPK() { }

    public ChecklistPK(UserEntity user_id, Long id) {
        this.user_id = user_id;
        this.id = id;
    }

    public ChecklistPK(UserEntity user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof ChecklistPK){
            ChecklistPK other = (ChecklistPK)obj;
            result = (
                    Objects.equals(this.id, other.id) &&
                            Objects.equals(this.user_id.getUser_id(), other.user_id.getUser_id()) &&
                            Objects.equals(this.user_id.getPassword(), other.user_id.getPassword()));
        }
        return result;
    }

    @Override
    public int hashCode(){
        return (
                this.id.hashCode() * 41 +
                this.user_id.getUser_id().hashCode() * 17 +
                this.user_id.getPassword().hashCode() * 41
        );
    }

}
