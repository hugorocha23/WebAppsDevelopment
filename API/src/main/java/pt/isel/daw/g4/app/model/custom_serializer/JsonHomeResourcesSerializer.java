package pt.isel.daw.g4.app.model.custom_serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pt.isel.daw.g4.app.model.json_home.JsonHomeResources;

import java.io.IOException;

public class JsonHomeResourcesSerializer extends StdSerializer<JsonHomeResources> {

    public JsonHomeResourcesSerializer() {
        this(null);
    }

    public JsonHomeResourcesSerializer(Class<JsonHomeResources> t) {
        super(t);
    }

    @Override
    public void serialize(JsonHomeResources value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        value.relations.forEach( elem -> {
            try {
                gen.writeObjectField(elem.jsonPropertyName, elem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gen.writeObjectField("hints", value.hints);

        gen.writeEndObject();
    }
}
