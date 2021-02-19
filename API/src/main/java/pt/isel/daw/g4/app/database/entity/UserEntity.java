package pt.isel.daw.g4.app.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "Users")
public class UserEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String user_id;

    @Column(name = "password")
    private String password;

    public UserEntity(String user_id, String password){
        this.user_id = user_id;
        this.password = password;
    }

    @Override
    public boolean equals(Object obj){
        boolean result = false;
        if(obj instanceof UserEntity){
            UserEntity other = (UserEntity)obj;
            result = (
                    Objects.equals(this.user_id, other.getUser_id()) &&
                    Objects.equals(this.password, other.getPassword())
            );
        }
        return result;
    }

    public UserEntity(){}

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
