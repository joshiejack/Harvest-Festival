package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.render.WeatherRenderer;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
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

import static joshie.harvest.calendar.SnowLoader.SNOW_LAYER;

public class HFWorldProvider extends WorldProviderSurface {
    @SideOnly(Side.CLIENT)
    private IRenderHandler WEATHER_RENDERER;

    @SideOnly(Side.CLIENT)
    @Override
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
        float brightness = worldObj.getSunBrightnessBody(f);
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

    public int getSkyBlendColour(World world, BlockPos center) {
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

        int original = HFTrackers.getCalendar(MCClientHelper.getWorld()).getSeasonData().getSkyColor();
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
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        float f1 = worldObj.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }


        int l = getSkyBlendColour(worldObj, new BlockPos(cameraEntity));
        float f4 = (float) (l >> 16 & 255) / 255.0F;
        float f5 = (float) (l >> 8 & 255) / 255.0F;
        float f6 = (float) (l & 255) / 255.0F;
        f4 *= f2;
        f5 *= f2;
        f6 *= f2;
        float f7 = Math.min(1F, worldObj.getRainStrength(partialTicks));
        float f8;
        float f9;

        if (f7 > 0.0F) {
            f8 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.6F;
            f9 = 1.0F - f7 * 0.75F;
            f4 = f4 * f9 + f8 * (1.0F - f9);
            f5 = f5 * f9 + f8 * (1.0F - f9);
            f6 = f6 * f9 + f8 * (1.0F - f9);
        }

        f8 = worldObj.getThunderStrength(partialTicks);

        if (f8 > 0.0F) {
            f8 /= 10F;
            f9 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.2F;
            float f10 = 1.0F - f8 * 0.75F;
            f4 = f4 * f10 + f9 * (1.0F - f10);
            f5 = f5 * f10 + f9 * (1.0F - f10);
            f6 = f6 * f10 + f9 * (1.0F - f10);
        }

        if (worldObj.getLastLightningBolt() > 0) {
            f9 = (float) worldObj.getLastLightningBolt() - partialTicks;

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

    public double clamp(double min, double max, double val) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Cheers to chylex for a bunch of the maths on this one ;D
     **/
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        Calendar calendar = HFTrackers.getCalendar(worldObj);
        if (calendar == null) return 1F;
        SeasonData data = calendar.getSeasonData();
        int time = (int) (worldTime % HFCalendar.TICKS_PER_DAY);
        double fac = data.getCelestialLengthFactor();
        float chylex = (float) (clamp(0, 1000D, time) + 11000D * (clamp(0, 11000D, time - 1000D) / 11000D) * fac + clamp(0, 1000D, time - 12000D) + 11000D * (clamp(0, 11000D, time - 12000D) / 11000D) * (2 - fac));
        float angle = (chylex / HFCalendar.TICKS_PER_DAY) - 0.25F;
        return angle + data.getCelestialAngleOffset();
    }

    @Override
    public boolean isBlockHighHumidity(BlockPos pos) {
        return HFApi.calendar.getDate(worldObj).getSeason() != Season.SUMMER && super.isBlockHighHumidity(pos);
    }

    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight) {
        Weather weather = HFApi.calendar.getWeather(worldObj);
        if (weather.isSnow()) {
            Biome biome = worldObj.getBiome(pos);
            float f = biome.getFloatTemperature(pos);

            if (f > 0.15F) {
                if (!checkLight) {
                    return true;
                } else {
                    if (biome.canRain()) {
                        if (pos.getY() >= 0 && pos.getY() < 256 && worldObj.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
                            IBlockState state = worldObj.getBlockState(pos);
                            if (state.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(worldObj, pos)) {
                                if (SNOW_LAYER != null && !biome.isSnowyBiome()) {
                                    HFApi.tickable.addTickable(worldObj, pos, SNOW_LAYER);
                                }

                                return true;
                            }
                        }
                    }

                    return false;
                }
            } else return super.canSnowAt(pos, checkLight);
        } else return false;
    }

    @Override
    public void updateWeather() {
        Calendar calendar = HFTrackers.getCalendar(worldObj);
        float rainStrength = calendar.getTodaysRainStrength();
        float thunderStrength = calendar.getTodaysStormStrength();
        if (worldObj.rainingStrength > rainStrength) {
            worldObj.rainingStrength -= 0.01F;
        } else if (worldObj.rainingStrength < rainStrength) {
            worldObj.rainingStrength += 0.01F;
        }

        if (worldObj.thunderingStrength > thunderStrength) {
            worldObj.thunderingStrength -= 0.1F;
        } else if (worldObj.thunderingStrength < thunderStrength) {
            worldObj.thunderingStrength += 0.01F;
        }
    }
}