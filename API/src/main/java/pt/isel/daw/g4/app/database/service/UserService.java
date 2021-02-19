package pt.isel.daw.g4.app.database.service;

import org.springframework.stereotype.Service;
import pt.isel.daw.g4.app.database.entity.UserEntity;
import pt.isel.daw.g4.app.database.repository.UserRepository;
import pt.isel.daw.g4.app.exceptions.DuplicatedPrimaryKeyException;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;
import pt.isel.daw.g4.app.model.input_model.UserInputModel;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts a new user in the database
     * @param input user credentials
     * @throws DuplicatedPrimaryKeyException when the username already exists in the database
     */
    public void createUser(UserInputModel input) throws DuplicatedPrimaryKeyException {
        Optional<UserEntity> usr = repo.findById(input.username);
        if(usr.isPresent())
            throw new DuplicatedPrimaryKeyException("The username "+input.username+" is already being used");
        repo.save(new UserEntity(input.username, input.password));
    }

    /**
     * Searches for an user in the database
     * @param userID username
     * @return encoded password
     * @throws ElementNotFoundException when the username does not exist in database
     * @throws UnsupportedEncodingException
     */
    public String getUser(String userID) throws ElementNotFoundException, UnsupportedEncodingException {
        Optional<UserEntity> usr = repo.findById(userID);
        if(!usr.isPresent())
            throw new ElementNotFoundException("The username "+userID+" is not registered");
        return new String(Base64.getEncoder().encode(usr.get().getPassword().getBytes()), "UTF-8");
    }
}
