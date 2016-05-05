package joshie.harvest.buildings.loader;

import com.google.gson.*;
import net.minecraft.util.text.TextComponentString;

import java.lang.reflect.Type;


public class TextComponentAdapter implements JsonSerializer<TextComponentString>, JsonDeserializer<TextComponentString> {
    @Override
    public JsonElement serialize(TextComponentString src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getText());
    }

    @Override
    public TextComponentString deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new TextComponentString(json.getAsJsonPrimitive().getAsString());
    }
}

