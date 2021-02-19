package pt.isel.daw.g4.app.model.output_model;

import pt.isel.daw.g4.app.model.collection_json.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChecklistTemplatesOutputModel extends CollectionJsonSchema{

    private static CollectionJsonTemplate thisTemplate;
    static {
        thisTemplate = new CollectionJsonTemplate(
                Arrays.asList(
                        new CollectionJsonData("name","Name"),
                        new CollectionJsonData("description","Description")
                ));
    }

    public ChecklistTemplatesOutputModel(){
        this.version = "1.0";
        this.href = "/checklist-template";
        this.template = thisTemplate;
    }

    public void generateItemData(Long id, String name, String description) {
        List<CollectionJsonData> itemData = Arrays.asList(
                new CollectionJsonData("name", name,"Name"),
                new CollectionJsonData("description",description,"Description")
        );
        String hrefItem = "/checklist-template/"+id+"/items";
        List<CollectionJsonLink> itemLinks = Collections.singletonList(new CollectionJsonLink("Template Items", "collection", hrefItem));
        items.add(new CollectionJsonItem("/checklist-template/"+id,itemData,itemLinks));
    }
}