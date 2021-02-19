package pt.isel.daw.g4.app.model.json_home;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class JsonHomeHints {

    public List<String> allow;

    @JsonInclude(Include.NON_NULL)
    public List<String> acceptPatch;

    @JsonInclude(Include.NON_NULL)
    public List<String> acceptRanges;
}
