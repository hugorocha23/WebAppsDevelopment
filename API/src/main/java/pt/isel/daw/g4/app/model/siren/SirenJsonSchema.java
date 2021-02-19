package pt.isel.daw.g4.app.model.siren;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public abstract class SirenJsonSchema {

    @JsonProperty("class")
    public List<String> natures = new ArrayList<>();;

    public Object properties;

    public List<SirenJsonEntity> entities = new ArrayList<>();;

    public List<SirenJsonAction> actions = new ArrayList<>();

    public List<SirenJsonLink> links = new ArrayList<>();;

}