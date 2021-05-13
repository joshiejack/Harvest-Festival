package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.block.Block;

import java.lang.reflect.Type;


public class OffsetAdapter implements JsonSerializer<Block.EnumOffsetType>, JsonDeserializer<Block.EnumOffsetType> {
    @Override
    public JsonElement serialize(Block.EnumOffsetType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }

    @Override
    public Block.EnumOffsetType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Block.EnumOffsetType.valueOf(json.getAsString().toUpperCase());
    }
}

