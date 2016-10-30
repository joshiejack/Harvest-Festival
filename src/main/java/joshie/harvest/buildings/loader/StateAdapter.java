package joshie.harvest.buildings.loader;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;


public class StateAdapter implements JsonSerializer<IBlockState>, JsonDeserializer<IBlockState> {
    @Override
    public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
        String text = Block.REGISTRY.getNameForObject(src.getBlock()).toString().replace("minecraft:", "");
        int meta = src.getBlock().getMetaFromState(src);
        if (meta != 0) text = text + " " + meta;
        return new JsonPrimitive(text);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String[] state = json.getAsString().split(" ");
        ResourceLocation block = new ResourceLocation(state[0]);
        int meta = state.length == 2 ? Integer.parseInt(state[1]) : 0;
        return Block.REGISTRY.getObject(block).getStateFromMeta(meta);
    }
}

