package pt.isel.daw.g4.app.model.output_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.daw.g4.app.model.ChecklistItemProperties;
import pt.isel.daw.g4.app.model.siren.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChecklistItemOutputModel extends SirenJsonSchema {

    @JsonIgnore
    private static final List<String> ITEM_CLASS = new ArrayList<>();
    @JsonIgnore
    private static final List<SirenJsonField> EDIT_ACTION_FIELDS = new ArrayList<>();
    static {
        ITEM_CLASS.add("checklist-item");

        EDIT_ACTION_FIELDS.add(new SirenJsonField("description", "text", "", "Description"));
        EDIT_ACTION_FIELDS.add(new SirenJsonField("complete","boolean", "", "Complete Item"));
    }

    public ChecklistItemOutputModel(Long checklistId, Long itemId, String name, String status, String description, Object checklistProperties, Long previousId, Long nextId)
    {
        this.natures = ITEM_CLASS;
        this.properties = new ChecklistItemProperties(itemId, checklistId, name, status, description);
        generateEntities(checklistProperties, checklistId);
        generateActions(checklistId, itemId);
        generateLinks(checklistId, itemId, previousId, nextId);
    }

    private void generateEntities(Object checklist_properties, Long checklistId) {
        this.entities.add(
                new SirenJsonSubEntity(
                        Collections.singletonList("checklist-items"),
                        Collections.singletonList("collection"),
                        "/checklist/"+checklistId+"/items"
                )
        );

        this.entities.add(
                new SirenJsonExternalEntity(
                        Collections.singletonList("checklist"),
                        Collections.singletonList("parent"),
                        checklist_properties,
                        Collections.singletonList(
                                new SirenJsonLink(
                                        Collections.singletonList("self"),
                                        "/checklist/"+checklistId)
                        )
                )
        );
    }

    private void generateActions(Long checklistId, Long itemId) {
        this.actions.add(new SirenJsonAction(
                "edit-checklist-item",
                "Edit Item",
                "PUT",
                "/checklist/" + checklistId + "/items/" + itemId + "/edit",
                "application/json",
                EDIT_ACTION_FIELDS
        ));
    }

    private void generateLinks(Long checklistId, Long itemId, Long previousId, Long nextId) {

        this.links.add(new SirenJsonLink(Collections.singletonList("self"),"/checklist/"+checklistId+"/items/"+itemId));

        if(previousId != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("previous"),"/checklist/"+checklistId+"/items/"+previousId));

        if(nextId != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("next"),"/checklist/"+checklistId+"/items/"+nextId));
    }
}
