package pt.isel.daw.g4.app.exceptions;

import org.springframework.http.HttpStatus;

public class ElementNotFoundException extends GenericProblemJsonException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;
    private static final String TITLE = "Not Found";
    private static final String TYPE = "about:blank";

    public ElementNotFoundException(String message){
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
