package pt.isel.daw.g4.app.model.output_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.daw.g4.app.model.ChecklistTemplateItemProperties;
import pt.isel.daw.g4.app.model.siren.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChecklistTemplateItemOutputModel extends SirenJsonSchema {

    @JsonIgnore
    private static final List<String> ITEM_CLASS = new ArrayList<>();
    @JsonIgnore
    private static final List<SirenJsonField> EDIT_ACTION_FIELDS = new ArrayList<>();
    static {
        ITEM_CLASS.add("checklist-template-item");

        EDIT_ACTION_FIELDS.add(new SirenJsonField("description", "text", "", "Description"));
    }

    public ChecklistTemplateItemOutputModel(Long templateId,
                                            Long itemId,
                                            String name,
                                            String description,
                                            Object templateProperties,
                                            Long previousId,
                                            Long nextId)
    {
        this.natures = ITEM_CLASS;
        this.properties = new ChecklistTemplateItemProperties(itemId, templateId, name, description);
        generateEntities(templateId, templateProperties);
        generateActions(templateId, itemId);
        generateLinks(templateId, itemId, previousId, nextId);
    }

    private void generateEntities(Long templateId, Object properties) {
        this.entities.add(
            new SirenJsonSubEntity(
                    Collections.singletonList("checklist-template-items"),
                    Collections.singletonList("collection"),
                    "/checklist-template/"+templateId+"/items"
            )
        );

        this.entities.add(
            new SirenJsonExternalEntity(
                Collections.singletonList("template_checklist"),
                Collections.singletonList("parent"),
                properties,
                Collections.singletonList(
                        new SirenJsonLink(
                                Collections.singletonList("self"),
                                "/checklist-template/"+templateId)
                )
            )
        );
    }

    private void generateActions(Long templateId, Long itemId) {
        this.actions.add(
            new SirenJsonAction(
                "edit-checklist-template-item",
                "Edit Item",
                "PUT",
                "/checklist-template/" + templateId + "/items/" + itemId + "/edit",
                "application/json",
                EDIT_ACTION_FIELDS
            )
        );
    }

    private void generateLinks(Long templateId, Long itemId, Long previousId, Long nextId) {
        this.links.add(new SirenJsonLink(Collections.singletonList("self"),"/checklist-template/"+templateId+"/items/"+itemId));

        if(previousId != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("previous"),"/checklist-template/"+templateId+"/items/"+previousId));

        if(nextId != null)
            this.links.add(new SirenJsonLink(Collections.singletonList("next"),"/checklist-template/"+templateId+"/items/"+nextId));
    }

}