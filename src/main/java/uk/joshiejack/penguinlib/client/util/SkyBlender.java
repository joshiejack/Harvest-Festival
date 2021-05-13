package uk.joshiejack.penguinlib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeModContainer;

public class SkyBlender {
    private static int skyX, skyZ;
    private static boolean skyInit;
    private static int skyRGBMultiplier;
    private static int original;

    public static int get(World world, BlockPos center, int original) {
        if (center.getX() == skyX && center.getZ() == skyZ && skyInit && SkyBlender.original == original) {
            return skyRGBMultiplier;
        }

        skyInit = true;

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && ranges.length > 0) {
            distance = ranges[MathHelper.clamp(settings.renderDistanceChunks, 0, ranges.length - 1)];
        }

        int r = 0;
        int g = 0;
        int b = 0;

        int divider = 0;
        for (int x = -distance; x <= distance; ++x) {
            for (int z = -distance; z <= distance; ++z) {
                BlockPos pos = center.add(x, 0, z);
                Biome biome = world.getBiome(pos);
                int colour = biome.getSkyColorByTemp(biome.getTemperature(pos));
                r += (colour & 0xFF0000) >> 16;
                g += (colour & 0x00FF00) >> 8;
                b += colour & 0x0000FF;
                r += ((original & 0xFF0000) >> 16) * 2;
                g += ((original & 0x00FF00) >> 8) * 2;
                b += (original & 0x0000FF) * 2;
                divider += 3;
            }
        }

        int multiplier = (r / divider & 255) << 16 | (g / divider & 255) << 8 | b / divider & 255;

        skyX = center.getX();
        skyZ = center.getZ();
        skyRGBMultiplier = multiplier;
        SkyBlender.original = original;
        return skyRGBMultiplier;
    }
}
