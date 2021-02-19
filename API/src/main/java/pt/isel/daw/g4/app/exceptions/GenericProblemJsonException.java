package pt.isel.daw.g4.app.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GenericProblemJsonException extends Exception{

    public abstract HttpStatus getStatus();
    public abstract String getTitle();
    public abstract String getType();

    public GenericProblemJsonException(String message){
        super(message);
    }
}
