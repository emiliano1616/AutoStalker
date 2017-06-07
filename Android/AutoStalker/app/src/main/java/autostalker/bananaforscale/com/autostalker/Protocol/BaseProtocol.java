package autostalker.bananaforscale.com.autostalker.Protocol;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Emiliano on 03/06/2017.
 */

public abstract class BaseProtocol {

    public String toJson(){
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Enum.tipoMensaje.class, new TipoMensajeSerializer());
        return gson.create().toJson(this);
//        return gson.toJson(this);
    }
}

class TipoMensajeSerializer implements JsonSerializer<Enum.tipoMensaje> {
    public JsonElement serialize(Enum.tipoMensaje src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive( src.toString());
    }
}

class TipoMensajeDeserializer implements JsonDeserializer<Enum.tipoMensaje> {
    public Enum.tipoMensaje deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if ( Enum.tipoMensaje.Movement.value == json.getAsInt()) {
            return Enum.tipoMensaje.Movement;
        }
        if ( Enum.tipoMensaje.Settings.value == json.getAsInt()) {
            return Enum.tipoMensaje.Settings;
        }
        if ( Enum.tipoMensaje.ReturnCommand.value == json.getAsInt()) {
            return Enum.tipoMensaje.ReturnCommand;
        }
//        if ( Enum.tipoMensaje.Movement.value == json.getAsInt()) {
//            return Enum.tipoMensaje.Movement;
//        }
        return null;
    }
}

