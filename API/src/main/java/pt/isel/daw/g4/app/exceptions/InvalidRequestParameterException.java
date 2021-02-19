package pt.isel.daw.g4.app.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequestParameterException extends GenericProblemJsonException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String TITLE = "Request contains invalid arguments";
    private static final String TYPE = "/errors/invalid-request-parameters";

    public InvalidRequestParameterException(String message) {
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
