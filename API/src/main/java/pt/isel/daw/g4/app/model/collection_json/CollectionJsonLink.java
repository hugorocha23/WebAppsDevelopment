package pt.isel.daw.g4.app.model.collection_json;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CollectionJsonLink {

    @JsonInclude(Include.NON_NULL)
    public String prompt;
    public String rel;
    public String href;

    public CollectionJsonLink(String prompt, String rel, String href){
        this.prompt = prompt;
        this.href = href;
        this.rel = rel;
    }
}
