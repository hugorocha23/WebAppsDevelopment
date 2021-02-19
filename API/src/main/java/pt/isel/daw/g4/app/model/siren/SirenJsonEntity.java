package pt.isel.daw.g4.app.model.siren;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SirenJsonEntity {

    @JsonProperty("class")
    public List<String> nature;

    public List<String> rel;

}
