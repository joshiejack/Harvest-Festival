package joshie.harvest.buildings.loader;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;


public class StateAdapter implements JsonSerializer<IBlockState>, JsonDeserializer<IBlockState> {
    @Override
    public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("name", new JsonPrimitive(Block.REGISTRY.getNameForObject(src.getBlock()).toString()));
        result.add("meta", new JsonPrimitive(src.getBlock().getMetaFromState(src)));
        return result;
    }

    @Override
    public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int meta = jsonObject.get("meta").getAsInt();
        return Block.REGISTRY.getObject(new ResourceLocation(name)).getStateFromMeta(meta);
    }
}

