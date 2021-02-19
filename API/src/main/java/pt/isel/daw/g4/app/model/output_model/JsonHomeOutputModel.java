package pt.isel.daw.g4.app.model.output_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.daw.g4.app.model.json_home.*;

import java.util.Arrays;

public class JsonHomeOutputModel extends JsonHomeSchema {

    @JsonIgnore
    private static JsonHomeAPI concreteApi = new JsonHomeAPI();

    @JsonIgnore
    private static JsonHomeLinkRelation rel1 = new JsonHomeLinkRelation();

    @JsonIgnore
    private static JsonHomeLinkRelation rel2 = new JsonHomeLinkRelation();

    @JsonIgnore
    private static JsonHomeHints hints = new JsonHomeHints();

    static {
        concreteApi.title = "Checklist Template API";
        concreteApi.links = new JsonHomeLinks();
        concreteApi.links.describedBy = "https://github.com/isel-leic-daw/1718-2-LI61D-G4/wiki";

        rel1.href = "/checklist";
        rel1.jsonPropertyName = "/checklist";

        rel2.href = "/checklist-template";
        rel2.jsonPropertyName = "/checklist-template";

        hints.allow = Arrays.asList( "GET", "POST" );
    }

    public JsonHomeOutputModel(){

        api = concreteApi;

        resources = new JsonHomeResources();
        resources.hints = hints;
        resources.relations = Arrays.asList( rel1, rel2);

    }

}
