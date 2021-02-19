package pt.isel.daw.g4.app.model.collection_json;

import java.util.ArrayList;
import java.util.List;

public abstract class CollectionJsonSchema {

    public String version;
    public String href;
    public List<CollectionJsonLink> links = new ArrayList<>();
    public List<CollectionJsonItem> items = new ArrayList<>();
    public CollectionJsonTemplate template;

}
