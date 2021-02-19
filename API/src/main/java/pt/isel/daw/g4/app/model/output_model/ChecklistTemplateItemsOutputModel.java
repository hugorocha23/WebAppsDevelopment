package pt.isel.daw.g4.app.model.output_model;

import pt.isel.daw.g4.app.model.collection_json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChecklistTemplateItemsOutputModel extends CollectionJsonSchema {

    private static CollectionJsonTemplate thisTemplate;
    static {
        thisTemplate = new CollectionJsonTemplate(Arrays.asList(
                new CollectionJsonData("name","Name"),
                new CollectionJsonData("description","Description")
        ));
    }

    public ChecklistTemplateItemsOutputModel(Long templateId){
        this.version = "1.0.0";
        this.href = "/checklist-template/"+templateId+"/items";
        this.links = Collections.singletonList(
                new CollectionJsonLink(
                        "Checklist Template",
                        "template-checklist",
                        "/checklist-template/"+templateId
                )
        );
        this.template = thisTemplate;
    }

    public void generateItemData(Long templateId, Long itemId, String itemName) {
        items.add(new CollectionJsonItem(
                "/checklist-template/"+templateId+"/items/"+itemId,
                Collections.singletonList(new CollectionJsonData("name", itemName,"Item Name")),
                new ArrayList<>()
        ));
    }
}
