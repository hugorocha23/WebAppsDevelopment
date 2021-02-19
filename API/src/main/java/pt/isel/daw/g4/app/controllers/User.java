package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g4.app.database.service.UserService;
import pt.isel.daw.g4.app.exceptions.ElementNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping(value = "/user")
public class User {

    private UserService userService;

    public User(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles GET HTTP requests for a specific user
     * @param res Servlet response
     * @param userID username
     * @return encoded password
     * @throws ElementNotFoundException when the username does not exist in database
     * @throws UnsupportedEncodingException
     */
    @GetMapping(value = "/{user-id}", produces = "text/plain")
    public String getUser(
            HttpServletResponse res,
            @PathVariable("user-id") String userID)
            throws UnsupportedEncodingException, ElementNotFoundException
    {
        String encodedPassword = userService.getUser(userID);
        res.setStatus(HttpServletResponse.SC_OK);
        return encodedPassword;
    }
}
