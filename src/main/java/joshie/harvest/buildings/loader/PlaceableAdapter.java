package joshie.harvest.buildings.loader;

import com.google.gson.*;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.lib.HFModInfo;

import java.lang.reflect.Type;


public class PlaceableAdapter implements JsonSerializer<Placeable>, JsonDeserializer<Placeable> {
    @Override
    public JsonElement serialize(Placeable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("isBlock", new JsonPrimitive(src instanceof PlaceableBlock));
        result.add("theData", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Placeable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString(); //Compatiblity with previous books

        String clazz = "";
        boolean isBlock = jsonObject.get("isBlock").getAsBoolean();
        if (type != null) {
            clazz = HFModInfo.JAVAPATH + "buildings.placeable." + (isBlock ? "blocks." : "entities.") + type;
        }

        try {
            Placeable placeable = context.deserialize(jsonObject.get("theData"), Class.forName(clazz));
            if (placeable == null) throw new NullPointerException(clazz);
            placeable.init(); //Init the placeable
            return placeable;
        } catch (ClassNotFoundException cnfe) {
            return null;
        }
    }
}

