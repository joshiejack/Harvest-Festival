package uk.joshiejack.harvestcore.water;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

public class WaterHandlerBiomeDictionaryType extends WaterHandler {
    private final BiomeDictionary.Type type;

    private WaterHandlerBiomeDictionaryType(String name, BiomeDictionary.Type type, boolean match) {
        super(name, match);
        this.type = type;
    }

    @Override
    public boolean isType(World world, BlockPos pos) {
        return BiomeDictionary.hasType(world.getBiome(pos), type) == match;
    }

    @PenguinLoader("biome_dictionary")
    public static class Builder extends WaterHandler.Builder {
        @Override
        public WaterHandler build(String name, String data) {
            boolean match = data.startsWith("!");
            BiomeDictionary.Type type = BiomeDictionary.Type.getType(data.replace("!", ""));
            return new WaterHandlerBiomeDictionaryType(name, type, match);
        }
    }
}
