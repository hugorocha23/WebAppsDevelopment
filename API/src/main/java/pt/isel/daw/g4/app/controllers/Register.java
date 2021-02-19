package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g4.app.database.service.UserService;
import pt.isel.daw.g4.app.exceptions.DuplicatedPrimaryKeyException;
import pt.isel.daw.g4.app.model.input_model.UserInputModel;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/register")
public class Register {

    private UserService userService;

    public Register(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles POST HTTP requests for the creation of users
     * @param res Servlet response
     * @param input user credentials
     * @throws DuplicatedPrimaryKeyException Throws if the username received already exist in the database
     */
    @PostMapping
    public void registerUser(
            HttpServletResponse res,
            @RequestBody UserInputModel input) throws DuplicatedPrimaryKeyException {
        userService.createUser(input);
        res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
