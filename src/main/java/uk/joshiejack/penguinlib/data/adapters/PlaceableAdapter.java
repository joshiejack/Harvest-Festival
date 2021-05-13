package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.PlaceableHelper;
import uk.joshiejack.penguinlib.template.blocks.PlaceableBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;
import java.util.Locale;


public class PlaceableAdapter implements JsonSerializer<Placeable>, JsonDeserializer<Placeable> {
    @Override
    public JsonElement serialize(Placeable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = context.serialize(src, src.getClass()).getAsJsonObject();
        if ((src.getClass() != PlaceableBlock.class)) {
            result.add("type", new JsonPrimitive(PlaceableHelper.TYPE_REGISTRY.inverse().get(src.getClass().getCanonicalName())));
        }

        //Special case air
        if (src instanceof PlaceableBlock) {
            if (((PlaceableBlock) src).getBlock() == Blocks.AIR) {
                result.remove("state");
            }

            if (!((PlaceableBlock)src).isInteractable()) {
                result.remove("interactable");
            }
        }

        return result;
    }

    @Override
    public Placeable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "block";
        String clazz = PlaceableHelper.TYPE_REGISTRY.get(type);//PenguinLib.JAVAPATH + ".template." + type.replace(".", ".Placeable");
        if (clazz == null) {
            if (type.startsWith("blocks.")) {
                clazz = PlaceableHelper.TYPE_REGISTRY.get(type.replace("blocks.", "").toLowerCase(Locale.ENGLISH));
            }

            if (type.startsWith("entities.")) {
                clazz = PlaceableHelper.TYPE_REGISTRY.get(type.replace("entities.", "").toLowerCase(Locale.ENGLISH));
            }

            if (type.equals("entities.ItemFrame")) clazz = PlaceableHelper.TYPE_REGISTRY.get("item_frame");
        }

        if (clazz == null) return null;

        try {
            if (type.equals("block")) {
                if (!jsonObject.has("state")) {
                    String[] pos = jsonObject.get("pos").getAsString().split(" ");
                    BlockPos thePos = new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]));
                    return new PlaceableBlock(Blocks.AIR.getDefaultState(), thePos);
                }
            }

            Placeable placeable = context.deserialize(jsonObject, Class.forName(clazz));
            if (placeable == null) throw new NullPointerException(clazz);
            return placeable;
        } catch (ClassNotFoundException cnfe) {
            return null;
        }
    }
}

