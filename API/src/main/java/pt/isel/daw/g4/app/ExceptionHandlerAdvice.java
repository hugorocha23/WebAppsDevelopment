package pt.isel.daw.g4.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.isel.daw.g4.app.exceptions.GenericProblemJsonException;
import pt.isel.daw.g4.app.model.problem_json.ProblemJsonSchema;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(GenericProblemJsonException.class)
    public final ResponseEntity<ProblemJsonSchema> handle(
            GenericProblemJsonException ex,
            HttpServletRequest req)
    {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        log.error(writer.toString());
        ProblemJsonSchema error = new ProblemJsonSchema();
        error.title = ex.getTitle();
        error.detail = ex.getMessage();
        error.type = ex.getType();
        error.instance = req.getRequestURI();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/problem+json");
        return new ResponseEntity<>(error, headers ,ex.getStatus());
    }
}
