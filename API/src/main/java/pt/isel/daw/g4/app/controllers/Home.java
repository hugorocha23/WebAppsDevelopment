package pt.isel.daw.g4.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.isel.daw.g4.app.model.output_model.JsonHomeOutputModel;

@RestController
@RequestMapping(value = "/")
public class Home {

    /**
     * Handles GET HTTP requests for the home page of the API
     * @return output model of json-home
     */
    @GetMapping(produces = "application/json-home")
    public JsonHomeOutputModel getHome()
    {
        return new JsonHomeOutputModel();
    }

}
