package uk.joshiejack.penguinlib.biome;

import uk.joshiejack.penguinlib.data.custom.biome.CustomBiomeData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeCustom extends Biome {
    private final CustomBiomeData data;

    public BiomeCustom(CustomBiomeData data) {
        super(buildProperties(data));
        this.data = data;
    }

    private static BiomeProperties buildProperties(CustomBiomeData data) {
        Biome.BiomeProperties properties = (new Biome.BiomeProperties(data.name)).setTemperature(data.temperature).setRainfall(data.rainfall);
        if (data.rainfall <= 0F) properties.setRainDisabled();
        if (data.snow) properties.setSnowEnabled();
        return properties;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return data.colors.getGrassColor() == -1 ? super.getGrassColorAtPos(pos) : data.colors.getGrassColor();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return data.colors.getFoliageColor() == -1 ? super.getFoliageColorAtPos(pos) : data.colors.getFoliageColor();
    }
}
