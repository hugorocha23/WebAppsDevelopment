package pt.isel.daw.g4.app.model.json_home;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class JsonHomeLinkRelation {

    @JsonInclude(Include.NON_NULL)
    public String href;

    @JsonInclude(Include.NON_NULL)
    public String hrefTemplate;

    @JsonInclude(Include.NON_NULL)
    public JsonHomeHrefVars hrefVars;

    @JsonIgnore
    public String jsonPropertyName;

}
