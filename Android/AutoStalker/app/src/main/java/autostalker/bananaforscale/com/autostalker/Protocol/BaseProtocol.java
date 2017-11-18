package autostalker.bananaforscale.com.autostalker.Protocol;

import com.google.gson.Gson;
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
        gson.registerTypeAdapter(Enum.messageType.class, new messageTypeSerializer());
        return gson.create().toJson(this);
//        return gson.toJson(this);
    }

    public static <T> T  fromJson(String json, Class<T>  type ){
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Enum.messageType.class, new messageTypeDeserializer());
        return gson.create().fromJson(json, type );
    }
}

class messageTypeSerializer implements JsonSerializer<Enum.messageType> {
    public JsonElement serialize(Enum.messageType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive( src.toString());
    }
}

class messageTypeDeserializer implements JsonDeserializer<Enum.messageType> {
    public Enum.messageType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if ( Enum.messageType.Movement.value == json.getAsInt()) {
            return Enum.messageType.Movement;
        }
        if ( Enum.messageType.Settings.value == json.getAsInt()) {
            return Enum.messageType.Settings;
        }
        if ( Enum.messageType.ReturnCommand.value == json.getAsInt()) {
            return Enum.messageType.ReturnCommand;
        }

        if ( Enum.messageType.BatteryLevel.value == json.getAsInt()) {
            return Enum.messageType.BatteryLevel;
        }

        if( Enum.messageType.ObstacleDetected.value == json.getAsInt()) {
            return Enum.messageType.ObstacleDetected;
        }
//        if ( Enum.messageType.Movement.value == json.getAsInt()) {
//            return Enum.messageType.Movement;
//        }
        return null;
    }
}

