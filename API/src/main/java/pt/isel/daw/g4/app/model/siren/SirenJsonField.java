package pt.isel.daw.g4.app.model.siren;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SirenJsonField {

    public String name;
    public String type;
    @JsonInclude(Include.NON_NULL)
    public String value;
    public String title;

    public SirenJsonField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public SirenJsonField(String name, String type, String value, String title) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.title = title;
    }
}
