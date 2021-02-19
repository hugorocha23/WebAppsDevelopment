package pt.isel.daw.g4.app.model.output_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.daw.g4.app.model.ChecklistProperties;
import pt.isel.daw.g4.app.model.siren.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChecklistOutputModel extends SirenJsonSchema {

    @JsonIgnore
    private static final List<String> CHECKLIST_CLASS = new ArrayList<>();
    @JsonIgnore
    private static final List<SirenJsonField> EDIT_ACTION_FIELDS = new ArrayList<>();
    static {
        CHECKLIST_CLASS.add("checklist");

        EDIT_ACTION_FIELDS.add(new SirenJsonField("description", "text", "", "Description"));
    }


    public ChecklistOutputModel(String name, String description, String dateToCompletion, String status, Long checklistId, Long previousID, Long nextID) {
        this.natures = CHECKLIST_CLASS;
        this.properties = new ChecklistProperties(name, description, dateToCompletion, status);
        generateActions(checklistId);
        generateEntities(checklistId);
        generateLinks(checklistId, previousID, nextID);
    }

    private void generateEntities(Long checklistId) {
        this.entities = Collections.singletonList(
                new SirenJsonSubEntity(Collections.singletonList("checklist-items"), Collections.singletonList("collection"), "/checklist/" + checklistId + "/items")
        );
    }

    private void generateActions(Long id) {
        this.actions.add(new SirenJsonAction(
                "edit-checklist",
                "Edit Checklist",
                "PUT",
                "/checklist/" + id + "/edit",
                "application/json",
                EDIT_ACTION_FIELDS
        ));
    }

    private void generateLinks(Long id, Long previousID, Long nextID) {
        this.links.add(new SirenJsonLink(Collections.singletonList("self"), "/checklist/" + id));

        if(previousID != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("prev"),"/checklist/" + previousID));

        if(nextID != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("next"),"/checklist/"+nextID));
    }
}
