package pt.isel.daw.g4.app.model.siren;

import java.util.List;

public class SirenJsonAction {

    public String name;
    public String title;
    public String method;
    public String href;
    public String type;
    public List<SirenJsonField> fields;

    public SirenJsonAction(String name, String title, String method, String href, String type, List<SirenJsonField> fields) {
        this.name = name;
        this.title = title;
        this.method = method;
        this.href = href;
        this.type = type;
        this.fields = fields;
    }

}
