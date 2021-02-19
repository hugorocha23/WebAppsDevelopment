package pt.isel.daw.g4.app.model.output_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.daw.g4.app.model.TemplateProperties;
import pt.isel.daw.g4.app.model.siren.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChecklistTemplateOutputModel extends SirenJsonSchema {

    @JsonIgnore
    private static final List<SirenJsonField> POST_ACTION_FIELDS = new ArrayList<>();
    @JsonIgnore
    private static final List<SirenJsonField> PUT_ACTION_FIELDS = new ArrayList<>();
    static {
        POST_ACTION_FIELDS.add(new SirenJsonField("name","text", "", "Name"));
        POST_ACTION_FIELDS.add(new SirenJsonField("description","text", "", "Description"));
        POST_ACTION_FIELDS.add(new SirenJsonField("completion_date","datetime", "", "Completion Date"));

        PUT_ACTION_FIELDS.add(new SirenJsonField("description", "text", "", "Description"));
    }

    public ChecklistTemplateOutputModel(Long id, String name, String description, Long previousID, Long nextID) {
        this.natures = Collections.singletonList("checklist-template");
        this.properties = new TemplateProperties(name, description);
        generateEntities(id);
        generateActions(id);
        generateLinks(id, previousID, nextID);
    }

    private void generateEntities(Long id) {
        List<String> nature = Collections.singletonList("checklist-template-items");
        List<String> rel = Collections.singletonList("collection");

        this.entities = Collections.singletonList(new SirenJsonSubEntity(nature, rel, "/checklist-template/" + id + "/items"));
    }

    private void generateActions(Long id) {
        this.actions.add(
                new SirenJsonAction(
                        "create-checklist-from-template",
                        "Create Checklist from Template",
                        "POST",
                        "/checklist-template/" + id,
                        "application/json",
                        POST_ACTION_FIELDS
                )
        );
        this.actions.add(
                new SirenJsonAction(
                        "edit-checklist-template",
                        "Edit Checklist Template",
                        "PUT",
                        "/checklist-template/" + id + "/edit",
                        "application/json",
                        PUT_ACTION_FIELDS
                )
        );
    }

    private void generateLinks(Long id, Long previousID, Long nextID) {
        this.links.add(new SirenJsonLink(Collections.singletonList("self"), "/checklist-template/" + id));

        if(previousID != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("prev"),"/checklist-template/" + previousID));

        if(nextID != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("next"),"/checklist-template/"+nextID));
    }
}
