package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.lang.reflect.Type;


public class TextComponentAdapter implements JsonSerializer<ITextComponent>, JsonDeserializer<ITextComponent> {
    @Override
    public JsonElement serialize(ITextComponent src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getUnformattedText());
    }

    @Override
    public ITextComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String text = json.getAsJsonPrimitive().getAsString();
        return text.startsWith("translate:") ? new TextComponentTranslation(text.replace("translate:", "")) : new TextComponentString(text);
    }
}

