package pt.isel.daw.g4.app.model.collection_json;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CollectionJsonData {

    public String name;
    public String value;
    @JsonInclude(Include.NON_NULL)
    public String prompt;

    public CollectionJsonData(String name, String value, String prompt){
        this.name = name;
        this.value = value;
        this.prompt = prompt;
    }

    public CollectionJsonData(String name, String prompt){
        this.name = name;
        value = "";
        this.prompt = prompt;
    }
}
