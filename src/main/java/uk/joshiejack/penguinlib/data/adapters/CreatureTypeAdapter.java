package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.entity.EnumCreatureType;

import java.lang.reflect.Type;
import java.util.Locale;


public class CreatureTypeAdapter implements JsonSerializer<EnumCreatureType>, JsonDeserializer<EnumCreatureType> {
    @Override
    public JsonElement serialize(EnumCreatureType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public EnumCreatureType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EnumCreatureType.valueOf(json.getAsString().toUpperCase(Locale.ENGLISH));
    }
}

