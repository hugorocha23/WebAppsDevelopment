package pt.isel.daw.g4.app.message_converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import pt.isel.daw.g4.app.model.output_model.JsonHomeOutputModel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

@Component
public class JsonHomeMessageConverter extends AbstractGenericHttpMessageConverter<JsonHomeOutputModel> {

    public JsonHomeMessageConverter(){
        super(new MediaType("application","json-home"));
    }

    @Override
    public boolean supports(Class<?> type) {

        return JsonHomeOutputModel.class.isAssignableFrom(type);
    }

    @Override
    protected void writeInternal(JsonHomeOutputModel jsonHomeOutputModel, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream stream = outputMessage.getBody();

        ObjectMapper mapper = new ObjectMapper();
        stream.write( mapper.writeValueAsBytes(jsonHomeOutputModel) );

        stream.flush();
        stream.close();
    }

    @Override
    protected JsonHomeOutputModel readInternal(Class<? extends JsonHomeOutputModel> clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        return null;
    }

    @Override
    public JsonHomeOutputModel read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        return null;
    }

}
