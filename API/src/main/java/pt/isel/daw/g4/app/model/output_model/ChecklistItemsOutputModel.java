package pt.isel.daw.g4.app.model.output_model;

import pt.isel.daw.g4.app.model.collection_json.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChecklistItemsOutputModel extends CollectionJsonSchema {

    private static CollectionJsonTemplate thisTemplate;
    static {
        thisTemplate = new CollectionJsonTemplate(Arrays.asList(
                new CollectionJsonData("name","Name"),
                new CollectionJsonData("description","Description")
        ));
    }

    public ChecklistItemsOutputModel(Long checklistId){
        this.version = "1.0.0";
        this.href = "/checklist/"+checklistId+"/items";
        this.links = Collections.singletonList(
                new CollectionJsonLink("Checklist",
                        "check-list",
                        "/checklist/"+checklistId
                )
        );
        this.template = thisTemplate;
    }

    public void generateItemData(Long checklistId, Long itemId, String itemName) {
        items.add(new CollectionJsonItem(
                "/checklist/" + checklistId + "/items/" + itemId,
                Collections.singletonList(new CollectionJsonData("name", itemName, "Item Name" )),
                new ArrayList<>()));
    }

}
