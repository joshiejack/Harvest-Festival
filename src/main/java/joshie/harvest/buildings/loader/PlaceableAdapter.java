package joshie.harvest.buildings.loader;

import com.google.gson.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;


public class PlaceableAdapter implements JsonSerializer<Placeable>, JsonDeserializer<Placeable> {
    @Override
    public JsonElement serialize(Placeable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = context.serialize(src, src.getClass()).getAsJsonObject();
        String prefix = src instanceof PlaceableBlock ? "blocks." : "entities.";
        if ((src.getClass() != PlaceableBlock.class)) {
            result.add("type", new JsonPrimitive(prefix + src.getClass().getSimpleName().replace("Placeable", "")));
        }

        //Special case air
        if (src instanceof PlaceableBlock) {
            if (((PlaceableBlock) src).getBlock() == Blocks.AIR) {
                result.remove("state");
            }
        }

        return result;
    }

    @Override
    public Placeable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "blocks.Block";
        String clazz = HFModInfo.JAVAPATH + "buildings.placeable." + type.replace(".", ".Placeable");

        try {
            if (type.equals("blocks.Block")) {
                if (!jsonObject.has("state")) {
                    String[] pos = jsonObject.get("pos").getAsString().split(" ");
                    BlockPos thePos = new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]));
                    return new PlaceableBlock(Blocks.AIR.getDefaultState(), thePos);
                }
            }

            Placeable placeable = context.deserialize(jsonObject, Class.forName(clazz));
            if (placeable == null) throw new NullPointerException(clazz);
            placeable.init(); //Init the placeable
            return placeable;
        } catch (ClassNotFoundException cnfe) {
            return null;
        }
    }
}

