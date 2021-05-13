package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.item.EnumAction;

import java.lang.reflect.Type;


public class ActionAdapter implements JsonSerializer<EnumAction>, JsonDeserializer<EnumAction> {
    @Override
    public JsonElement serialize(EnumAction src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }

    @Override
    public EnumAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EnumAction.valueOf(json.getAsString().toUpperCase());
    }
}

