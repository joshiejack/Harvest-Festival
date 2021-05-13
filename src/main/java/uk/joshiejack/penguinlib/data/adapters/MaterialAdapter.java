package uk.joshiejack.penguinlib.data.adapters;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.*;
import net.minecraft.block.material.Material;

import java.lang.reflect.Type;


public class MaterialAdapter implements JsonSerializer<Material>, JsonDeserializer<Material> {
    private static final BiMap<String, Material> byName = HashBiMap.create();
    public static Material byName(String name) {
        if (byName.isEmpty())
            byName.putAll(new MaterialAdapter().byName());
        return byName.get(name);
    }

    private BiMap<String, Material> byName() {
        final BiMap<String, Material> byName = HashBiMap.create();
            byName.put("air", Material.AIR);
            byName.put("grass", Material.GRASS);
            byName.put("ground", Material.GROUND);
            byName.put("wood", Material.WOOD);
            byName.put("rock", Material.ROCK);
            byName.put("iron", Material.IRON);
            byName.put("anvil", Material.ANVIL);
            byName.put("water", Material.WATER);
            byName.put("lava", Material.LAVA);
            byName.put("leaves", Material.LEAVES);
            byName.put("plants", Material.PLANTS);
            byName.put("vine", Material.VINE);
            byName.put("sponge", Material.SPONGE);
            byName.put("cloth", Material.CLOTH);
            byName.put("fire", Material.FIRE);
            byName.put("sand", Material.SAND);
            byName.put("circuits", Material.CIRCUITS);
            byName.put("carpet", Material.CARPET);
            byName.put("glass", Material.GLASS);
            byName.put("redstone_light", Material.REDSTONE_LIGHT);
            byName.put("tnt", Material.TNT);
            byName.put("coral", Material.CORAL);
            byName.put("ice", Material.ICE);
            byName.put("packed_ice", Material.PACKED_ICE);
            byName.put("snow", Material.SNOW);
            byName.put("crafted_snow", Material.CRAFTED_SNOW);
            byName.put("cactus", Material.CACTUS);
            byName.put("clay", Material.CLAY);
            byName.put("gourd", Material.GOURD);
            byName.put("dragon_egg", Material.DRAGON_EGG);
            byName.put("portal", Material.PORTAL);
            byName.put("material", Material.CAKE);
            byName.put("web", Material.WEB);
            return byName;
    }


    @Override
    public JsonElement serialize(Material src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(byName().inverse().get(src).toLowerCase());
    }

    @Override
    public Material deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return byName().get(json.getAsString());
    }
}

