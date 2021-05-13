package uk.joshiejack.penguinlib.data.custom.biome;

import uk.joshiejack.penguinlib.biome.BiomeCustom;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("biome:standard")
public class CustomBiomeData extends AbstractCustomData<Biome, CustomBiomeData> {
    public Colors colors = new Colors();
    public float temperature;
    public float rainfall;
    public boolean snow = false;

    @Nonnull
    @Override
    public Biome build(ResourceLocation registryName, @Nonnull CustomBiomeData data, @Nullable CustomBiomeData... unused) {
        return new BiomeCustom(data).setRegistryName(registryName);
    }

    public static class Colors {
        public String grass = null;
        public String foliage = null;
        private transient int grassColor = -1;
        private transient int foliageColor = -1;

        public int getGrassColor() {
            if (grass != null && grassColor == -1) {
                grassColor = Integer.parseInt(grass.replace("0x", ""), 16);
            }

            return grassColor;
        }

        public int getFoliageColor() {
            if (foliage != null && foliageColor == -1) {
                foliageColor = Integer.parseInt(foliage.replace("0x", ""), 16);
            }

            return foliageColor;
        }
    }
}
