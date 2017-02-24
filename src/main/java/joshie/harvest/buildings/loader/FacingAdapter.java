package joshie.harvest.buildings.loader;

import com.google.gson.*;
import net.minecraft.util.EnumFacing;

import java.lang.reflect.Type;


public class FacingAdapter implements JsonSerializer<EnumFacing>, JsonDeserializer<EnumFacing> {
    @Override
    public JsonElement serialize(EnumFacing src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }

    @Override
    public EnumFacing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EnumFacing.valueOf(json.getAsString().toUpperCase());
    }
}

