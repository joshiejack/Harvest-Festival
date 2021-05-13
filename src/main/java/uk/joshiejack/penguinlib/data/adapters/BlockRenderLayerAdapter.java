package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.util.BlockRenderLayer;

import java.lang.reflect.Type;
import java.util.Locale;


public class BlockRenderLayerAdapter implements JsonSerializer<BlockRenderLayer>, JsonDeserializer<BlockRenderLayer> {
    @Override
    public JsonElement serialize(BlockRenderLayer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public BlockRenderLayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return BlockRenderLayer.valueOf(json.getAsString().toUpperCase(Locale.ENGLISH));
    }
}

