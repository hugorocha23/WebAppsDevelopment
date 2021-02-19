package pt.isel.daw.g4.app.model.collection_json;

import java.util.List;

public class CollectionJsonItem {

    public String href;
    public List<CollectionJsonData> data;
    public List<CollectionJsonLink> links;

    public CollectionJsonItem(String href, List<CollectionJsonData> data, List<CollectionJsonLink> links) {
        this.href = href;
        this.data = data;
        this.links = links;
    }
    public CollectionJsonItem(){}
}
