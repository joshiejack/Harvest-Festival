package uk.joshiejack.penguinlib.data.adapters;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.*;
import net.minecraft.block.SoundType;

import java.lang.reflect.Type;


public class SoundTypeAdapter implements JsonSerializer<SoundType>, JsonDeserializer<SoundType> {
    private BiMap<String, SoundType> byName() {
        final BiMap<String, SoundType> byName = HashBiMap.create();
        byName.put("wood", SoundType.WOOD);
        byName.put("ground", SoundType.GROUND);
        byName.put("plant", SoundType.PLANT);
        byName.put("stone", SoundType.STONE);
        byName.put("metal", SoundType.METAL);
        byName.put("glass", SoundType.GLASS);
        byName.put("cloth", SoundType.CLOTH);
        byName.put("sand", SoundType.SAND);
        byName.put("snow", SoundType.SNOW);
        byName.put("ladder", SoundType.LADDER);
        byName.put("anvil", SoundType.ANVIL);
        byName.put("slime", SoundType.SLIME);
        return byName;
    }

    @Override
    public JsonElement serialize(SoundType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(byName().inverse().get(src).toLowerCase());
    }

    @Override
    public SoundType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return byName().get(json.getAsString());
    }
}

