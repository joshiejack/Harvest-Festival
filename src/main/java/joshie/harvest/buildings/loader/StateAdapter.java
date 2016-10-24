package joshie.harvest.buildings.loader;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;


public class StateAdapter implements JsonSerializer<IBlockState>, JsonDeserializer<IBlockState> {
    @Override
    public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Block.REGISTRY.getNameForObject(src.getBlock()).toString() + " " + src.getBlock().getMetaFromState(src));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String[] state = json.getAsString().split(" ");
        return Block.REGISTRY.getObject(new ResourceLocation(state[0])).getStateFromMeta(Integer.parseInt(state[1]));
    }
}

