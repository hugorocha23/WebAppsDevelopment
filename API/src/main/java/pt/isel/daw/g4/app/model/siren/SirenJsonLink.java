package pt.isel.daw.g4.app.model.siren;

import java.util.List;

public class SirenJsonLink {

    public List<String> rel;
    public String href;

    public SirenJsonLink(List<String> rel, String href) {
        this.rel = rel;
        this.href = href;
    }
}
