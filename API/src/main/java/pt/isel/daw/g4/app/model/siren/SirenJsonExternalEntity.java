package pt.isel.daw.g4.app.model.siren;

import java.util.List;

public class SirenJsonExternalEntity extends SirenJsonEntity{

    public Object properties;
    public List<SirenJsonLink> links;

    public SirenJsonExternalEntity(List<String> nature, List<String> rel, Object properties, List<SirenJsonLink> links) {
        this.nature = nature;
        this.rel = rel;
        this.properties = properties;
        this.links = links;
    }
}
