package pt.isel.daw.g4.app.model.output_model;

import pt.isel.daw.g4.app.model.collection_json.*;

import java.util.Arrays;
import java.util.Collections;

public class ChecklistsOutputModel extends CollectionJsonSchema {

    private static CollectionJsonTemplate thisTemplate;
    static {
        thisTemplate = new CollectionJsonTemplate(
                Arrays.asList(
                        new CollectionJsonData("name","","Checklist Name"),
                        new CollectionJsonData("description","","Description"),
                        new CollectionJsonData("completion_date","","Completion Date")
                ));
    }

    public ChecklistsOutputModel(){
        this.version = "1.0.0";
        this.href = "/checklist";
        this.template = thisTemplate;
    }

    public void generateItemData(Long id, String name, String dateToCompletion) {
         items.add(new CollectionJsonItem("/checklist/"+id,
                Arrays.asList(
                        new CollectionJsonData("name", name, "Name"),
                        new CollectionJsonData("completion_date", dateToCompletion, "Completion Date")
                ),
                Collections.singletonList(
                        new CollectionJsonLink("Checklist Items", "collection", "/checklist/" + id + "/items")
                ))
         );
    }
}
