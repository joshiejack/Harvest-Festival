package harvestmoon.calendar;

import static harvestmoon.HarvestMoon.handler;
import static harvestmoon.helpers.CalendarHelper.getClientSeason;
import static harvestmoon.helpers.CalendarHelper.getSeason;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WeatherProvider extends WorldProviderSurface {
    private static final IRenderHandler weather = new WeatherRenderer();

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderHandler getWeatherRenderer() {
        return weather;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getStarBrightness(float f) {
        return getClientSeason() == Season.WINTER ? super.getStarBrightness(f) * 1.25F : super.getStarBrightness(f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        float f1 = worldObj.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        int i = MathHelper.floor_double(cameraEntity.posX);
        int j = MathHelper.floor_double(cameraEntity.posY);
        int k = MathHelper.floor_double(cameraEntity.posZ);
        int l = getClientSeason().getSkyColor();
        float f4 = (float) (l >> 16 & 255) / 255.0F;
        float f5 = (float) (l >> 8 & 255) / 255.0F;
        float f6 = (float) (l & 255) / 255.0F;
        f4 *= f2;
        f5 *= f2;
        f6 *= f2;
        float f7 = worldObj.getRainStrength(partialTicks);
        float f8;
        float f9;

        if (f7 > 0.0F) {
            f8 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.6F;
            f9 = 1.0F - f7 * 0.75F;
            f4 = f4 * f9 + f8 * (1.0F - f9);
            f5 = f5 * f9 + f8 * (1.0F - f9);
            f6 = f6 * f9 + f8 * (1.0F - f9);
        }

        f8 = worldObj.getWeightedThunderStrength(partialTicks);

        if (f8 > 0.0F) {
            f9 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.2F;
            float f10 = 1.0F - f8 * 0.75F;
            f4 = f4 * f10 + f9 * (1.0F - f10);
            f5 = f5 * f10 + f9 * (1.0F - f10);
            f6 = f6 * f10 + f9 * (1.0F - f10);
        }

        if (worldObj.lastLightningBolt > 0) {
            f9 = (float) worldObj.lastLightningBolt - partialTicks;

            if (f9 > 1.0F) {
                f9 = 1.0F;
            }

            f9 *= 0.45F;
            f4 = f4 * (1.0F - f9) + 0.8F * f9;
            f5 = f5 * (1.0F - f9) + 0.8F * f9;
            f6 = f6 * (1.0F - f9) + 1.0F * f9;
        }

        return Vec3.createVectorHelper((double) f4, (double) f5, (double) f6);
    }

    private float[] colorsSunriseSunset = new float[4];

    @SideOnly(Side.CLIENT)
    @Override
    public float[] calcSunriseSunsetColors(float p_76560_1_, float partialTicks) {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(p_76560_1_ * (float) Math.PI * 2.0F) - 0.0F;
        float f4 = -0.0F;

        if (f3 >= f4 - f2 && f3 <= f4 + f2) {
            float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float) Math.PI)) * 0.99F;
            f6 *= f6;
            colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
            colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
            colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
            colorsSunriseSunset[3] = f6;
            return colorsSunriseSunset;
        } else {
            return null;
        }
    }

    public double clamp(double min, double max, double val) {
        return Math.max(min, Math.min(max, val));
    }

    //Cheers to chylex for a bunch of the maths on this one ;D
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        Season season = handler.getServer() != null ? getSeason() : Season.SPRING;
        int time = (int) (worldTime % 24000L);
        double fac = season.getLengthFactor();
        float chylex = (float) (clamp(0, 1000D, time) + 11000D * (clamp(0, 11000D, time - 1000D) / 11000D) * fac + clamp(0, 1000D, time - 12000D) + 11000D * (clamp(0, 11000D, time - 12000D) / 11000D) * (2 - fac));
        float angle = (chylex / 24000) - 0.25F;
        return angle + season.getAngleOffset();
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        return getSeason() == Season.SUMMER ? false : super.isBlockHighHumidity(x, y, z);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        if (getSeason() == Season.WINTER) {
            BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(x, z);
            float f = biomegenbase.getFloatTemperature(x, y, z);

            if (f > 0.15F) {
                if (!checkLight) {
                    return true;
                } else {
                    if (biomegenbase.canSpawnLightningBolt()) {
                        if (y >= 0 && y < 256 && worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10) {
                            Block block = worldObj.getBlock(x, y, z);

                            if (block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(worldObj, x, y, z)) {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            } else return super.canSnowAt(x, y, z, checkLight);
        } else return super.canSnowAt(x, y, z, checkLight);
    }

    @Override
    public void updateWeather() {
        if (!worldObj.provider.hasNoSky) {
            if (!worldObj.isRemote) {
                if(worldObj.isRaining()) {
                    handler.getServer().getCropTracker().doRain();
                }
                
                Season season = getSeason();
                int i = worldObj.worldInfo.getThunderTime();

                if (i <= 0) {
                    if (worldObj.worldInfo.isThundering()) {
                        worldObj.worldInfo.setThunderTime(worldObj.rand.nextInt(season.getEndChance()) + 3600);
                    } else {
                        worldObj.worldInfo.setThunderTime(worldObj.rand.nextInt(season.getStartChance()) + 12000);
                    }
                } else {
                    i = i - season.getThunderTick();
                    worldObj.worldInfo.setThunderTime(i);

                    if (i <= 0) {
                        worldObj.worldInfo.setThundering(!worldObj.worldInfo.isThundering());
                    }
                }

                worldObj.prevThunderingStrength = worldObj.thunderingStrength;

                if (worldObj.worldInfo.isThundering()) {
                    worldObj.thunderingStrength = (float) ((double) worldObj.thunderingStrength + 0.01D);
                } else {
                    worldObj.thunderingStrength = (float) ((double) worldObj.thunderingStrength - 0.01D);
                }

                worldObj.thunderingStrength = MathHelper.clamp_float(worldObj.thunderingStrength, 0.0F, 1.0F);
                int j = worldObj.worldInfo.getRainTime();

                if (j <= 0) {
                    if (worldObj.worldInfo.isRaining()) {
                        worldObj.worldInfo.setRainTime(worldObj.rand.nextInt(season.getEndChance()) + 12000);
                    } else {
                        worldObj.worldInfo.setRainTime(worldObj.rand.nextInt(season.getStartChance()) + 12000);
                    }
                } else {
                    j = j - season.getRainTick();
                    worldObj.worldInfo.setRainTime(j);

                    if (j <= 0) {
                        worldObj.worldInfo.setRaining(!worldObj.worldInfo.isRaining());
                    }
                }

                worldObj.prevRainingStrength = worldObj.rainingStrength;

                if (worldObj.worldInfo.isRaining()) {
                    worldObj.rainingStrength = (float) ((double) worldObj.rainingStrength + 0.01D);
                } else {
                    worldObj.rainingStrength = (float) ((double) worldObj.rainingStrength - 0.01D);
                }

                worldObj.rainingStrength = MathHelper.clamp_float(worldObj.rainingStrength, 0.0F, 1.0F);
            }
        }
    }
}
