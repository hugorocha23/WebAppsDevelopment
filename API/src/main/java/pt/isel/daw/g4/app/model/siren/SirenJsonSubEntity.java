package pt.isel.daw.g4.app.model.siren;

import java.util.List;

public class SirenJsonSubEntity extends SirenJsonEntity{

    public String href;

    public SirenJsonSubEntity(List<String> nature, List<String> rel, String href) {
        this.nature = nature;
        this.rel = rel;
        this.href = href;
    }
}
