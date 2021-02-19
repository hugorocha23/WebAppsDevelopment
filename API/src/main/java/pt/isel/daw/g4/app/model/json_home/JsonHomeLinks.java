package pt.isel.daw.g4.app.model.json_home;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class JsonHomeLinks {

    @JsonInclude(Include.NON_NULL)
    public String author;

    @JsonInclude(Include.NON_NULL)
    public String describedBy;

}
