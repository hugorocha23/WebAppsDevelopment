package pt.isel.daw.g4.app.model.json_home;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pt.isel.daw.g4.app.model.custom_serializer.JsonHomeResourcesSerializer;

import java.util.List;

@JsonSerialize(using = JsonHomeResourcesSerializer.class)
public class JsonHomeResources {

    public List<JsonHomeLinkRelation> relations;

    public JsonHomeHints hints;

}
