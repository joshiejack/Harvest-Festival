package joshie.harvest.calendar.provider;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.SnowLoader;
import joshie.harvest.calendar.data.Calendar;
import joshie.harvest.calendar.render.WeatherRenderer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class HFWorldProvider extends WorldProviderSurface {
    @SideOnly(Side.CLIENT)
    private IRenderHandler WEATHER_RENDERER;

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public IRenderHandler getWeatherRenderer() {
        if (WEATHER_RENDERER != null) return WEATHER_RENDERER;
        else WEATHER_RENDERER = new WeatherRenderer();
        return WEATHER_RENDERER;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getStarBrightness(float f) {
        float brightness = super.getStarBrightness(f);
        return HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate().getSeason() == Season.WINTER ? brightness * 1.25F : brightness;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getSunBrightness(float f) {
        float brightness = world.getSunBrightnessBody(f);
        return HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate().getSeason() == Season.SUMMER ? brightness * 1.25F : brightness;
    }

    private static int skyX, skyZ;
    private static boolean skyInit;
    private static int skyRGBMultiplier;

    public static void reset() {
        skyX = 0;
        skyZ = 0;
        skyRGBMultiplier = 0;
        skyInit = false;
    }

    private int getSkyBlendColour(World world, BlockPos center) {
        if (center.getX() == skyX && center.getZ() == skyZ && skyInit) {
            return skyRGBMultiplier;
        }

        skyInit = true;

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        int[] ranges = ForgeModContainer.blendRanges;
        int distance = 0;
        if (settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length) {
            distance = ranges[settings.renderDistanceChunks];
        }

        int original = HFTrackers.getCalendar(MCClientHelper.getWorld()).getSeasonData().skyColor;
        int r = (original & 0xFF0000) >> 16;
        int g = (original & 0x00FF00) >> 8;
        int b = original & 0x0000FF;
        r += (original & 0xFF0000) >> 16;
        g += (original & 0x00FF00) >> 8;
        b += original & 0x0000FF;

        int divider = 2;
        for (int x = -distance; x <= distance; ++x) {
            for (int z = -distance; z <= distance; ++z) {
                BlockPos pos = center.add(x, 0, z);
                Biome biome = world.getBiome(pos);
                int colour = biome.getSkyColorByTemp(biome.getFloatTemperature(pos));
                r += (colour & 0xFF0000) >> 16;
                g += (colour & 0x00FF00) >> 8;
                b += colour & 0x0000FF;
                r += (original & 0xFF0000) >> 16;
                g += (original & 0x00FF00) >> 8;
                b += original & 0x0000FF;
                divider+= 2;
            }
        }

        int multiplier = (r / divider & 255) << 16 | (g / divider & 255) << 8 | b / divider & 255;

        skyX = center.getX();
        skyZ = center.getY();
        skyRGBMultiplier = multiplier;
        return skyRGBMultiplier;
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public Vec3d getSkyColor(@Nonnull Entity cameraEntity, float partialTicks) {
        float f1 = world.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }


        int l = getSkyBlendColour(world, new BlockPos(cameraEntity));
        float f4 = (float) (l >> 16 & 255) / 255.0F;
        float f5 = (float) (l >> 8 & 255) / 255.0F;
        float f6 = (float) (l & 255) / 255.0F;
        f4 *= f2;
        f5 *= f2;
        f6 *= f2;
        float f7 = Math.min(1F, world.getRainStrength(partialTicks));
        float f8;
        float f9;

        if (f7 > 0.0F) {
            f8 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.6F;
            f9 = 1.0F - f7 * 0.75F;
            f4 = f4 * f9 + f8 * (1.0F - f9);
            f5 = f5 * f9 + f8 * (1.0F - f9);
            f6 = f6 * f9 + f8 * (1.0F - f9);
        }

        f8 = world.getThunderStrength(partialTicks);

        if (f8 > 0.0F) {
            f8 /= 10F;
            f9 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.2F;
            float f10 = 1.0F - f8 * 0.75F;
            f4 = f4 * f10 + f9 * (1.0F - f10);
            f5 = f5 * f10 + f9 * (1.0F - f10);
            f6 = f6 * f10 + f9 * (1.0F - f10);
        }

        if (world.getLastLightningBolt() > 0) {
            f9 = (float) world.getLastLightningBolt() - partialTicks;

            if (f9 > 1.0F) {
                f9 = 1.0F;
            }

            f9 *= 0.45F;
            f4 = f4 * (1.0F - f9) + 0.8F * f9;
            f5 = f5 * (1.0F - f9) + 0.8F * f9;
            f6 = f6 * (1.0F - f9) + 1.0F * f9;
        }

        return new Vec3d((double) f4, (double) f5, (double) f6);
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        Calendar calendar = HFTrackers.getCalendar(world);
        if (calendar != null) {
            return calendar.getSeasonData().getCelestialAngle(worldTime % TICKS_PER_DAY);
        }

        return 1F;
    }

    @Override
    public boolean isBlockHighHumidity(@Nonnull BlockPos pos) {
        return HFApi.calendar.getDate(world).getSeason() != Season.SUMMER && super.isBlockHighHumidity(pos);
    }

    private boolean isWater(BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }

    @Override
    public boolean canBlockFreeze(@Nonnull BlockPos pos, boolean byWater) {
        Biome biome = world.getBiome(pos);
        if (!biome.canRain() || biome.isHighHumidity()) {
            return super.canBlockFreeze(pos, byWater);
        } else if (biome.isSnowyBiome()) {
            Weather weather = HFApi.calendar.getWeather(world);
            return !weather.isRain() && super.canBlockFreeze(pos, byWater);
        } else {
            Weather weather = HFApi.calendar.getWeather(world);
            float f = biome.getFloatTemperature(pos);
            if (weather.isSnow() && f > 0.15F) {
                if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
                    IBlockState iblockstate = world.getBlockState(pos);
                    Block block = iblockstate.getBlock();
                    if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
                        if (!byWater) {
                            HFApi.tickable.addTickable(world, pos, SnowLoader.INSTANCE);
                            return true;
                        }

                        boolean flag = isWater(pos.west()) && isWater(pos.east()) && isWater(pos.north()) && isWater(pos.south());
                        if (!flag) {
                            HFApi.tickable.addTickable(world, pos, SnowLoader.INSTANCE);
                            return true;
                        }
                    }
                }

                return false;
            } else return super.canBlockFreeze(pos, byWater);
        }
    }

    @Override
    public boolean canSnowAt(@Nonnull BlockPos pos, boolean checkLight) {
        Biome biome = world.getBiome(pos);
        if (!biome.canRain() || biome.isHighHumidity()) {
            return super.canSnowAt(pos, checkLight);
        } else if (biome.isSnowyBiome()) {
            Weather weather = HFApi.calendar.getWeather(world);
            return !weather.isRain() && super.canSnowAt(pos, checkLight);
        } else {
            Weather weather = HFApi.calendar.getWeather(world);
            float f = biome.getFloatTemperature(pos);
            if (weather.isSnow() && f > 0.15F) {
                if (!checkLight) {
                    return true;
                } else {
                    if (pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
                        IBlockState iblockstate = world.getBlockState(pos);
                        if (iblockstate.getBlock() != Blocks.SNOW_LAYER && iblockstate.getBlock().isReplaceable(world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos)) {
                            HFApi.tickable.addTickable(world, pos, SnowLoader.INSTANCE);
                            return true;
                        }
                    }

                    return false;
                }
            } else return super.canSnowAt(pos, checkLight);
        }
    }

    @Override
    public void updateWeather() {
        Calendar calendar = HFTrackers.getCalendar(world);
        int targetRain = calendar.getTodaysRainStrength();
        int targetStorm = calendar.getTodaysStormStrength();
        int rainStrength = (int) (world.rainingStrength * 100);
        if (rainStrength > targetRain) {
            world.rainingStrength -= 0.01F;
        } else if (rainStrength < targetRain) {
            world.rainingStrength += 0.01F;
        }

        int thunderStrength = (int) (world.thunderingStrength * 100);
        if (thunderStrength > targetStorm) {
            world.thunderingStrength -= 0.1F;
        } else if (thunderStrength < targetStorm) {
            world.thunderingStrength += 0.01F;
        }
    }
}