package uk.joshiejack.penguinlib.scripting.wrappers;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeJS extends AbstractJS<Biome> {
    public BiomeJS(Biome biome) {
        super(biome);
    }

    public boolean snows() {
        return penguinScriptingObject.getDefaultTemperature() < 0.15F;
    }

    public boolean is (String name) {
        return penguinScriptingObject.getRegistryName().toString().equals(name);
    }

    public boolean isType (String type) {
        return BiomeDictionary.hasType(penguinScriptingObject, BiomeDictionary.Type.getType(type));
    }
}
