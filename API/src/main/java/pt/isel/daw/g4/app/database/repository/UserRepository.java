package pt.isel.daw.g4.app.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.isel.daw.g4.app.database.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> { }
