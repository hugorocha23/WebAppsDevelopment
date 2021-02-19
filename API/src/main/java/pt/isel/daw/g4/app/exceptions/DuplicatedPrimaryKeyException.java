package pt.isel.daw.g4.app.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicatedPrimaryKeyException extends GenericProblemJsonException{

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private static final String TITLE = "Conflict";
    private static final String TYPE = "/errors/duplicated";

    public DuplicatedPrimaryKeyException(String message){
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return STATUS;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
