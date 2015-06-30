package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.config.Calendar;
import joshie.harvest.core.handlers.HFTrackers;
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
        float brightness = super.getStarBrightness(f);
        return HFTrackers.getCalendar().getDate().getSeason() == Season.WINTER ? brightness * 1.25F : brightness;
    }

    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float f) {
        float brightness = worldObj.getSunBrightnessBody(f);
        return HFTrackers.getCalendar().getDate().getSeason() == Season.SUMMER ? brightness * 1.25F : brightness;
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
        int l = HFTrackers.getCalendar().getSeasonData().getSkyColor();
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

        f8 = worldObj.getWeightedThunderStrength(partialTicks);

        if (f8 > 0.0F) {
            f8 /= 10F;
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

    public double clamp(double min, double max, double val) {
        return Math.max(min, Math.min(max, val));
    }

    /** Cheers to chylex for a bunch of the maths on this one ;D **/
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        ISeasonData data = HFTrackers.getCalendar().getDate().getSeasonData();
        int time = (int) (worldTime % Calendar.TICKS_PER_DAY);
        double fac = data.getCelestialLengthFactor();
        float chylex = (float) (clamp(0, 1000D, time) + 11000D * (clamp(0, 11000D, time - 1000D) / 11000D) * fac + clamp(0, 1000D, time - 12000D) + 11000D * (clamp(0, 11000D, time - 12000D) / 11000D) * (2 - fac));
        float angle = (chylex / Calendar.TICKS_PER_DAY) - 0.25F;
        return angle + data.getCelestialAngleOffset();
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        return HFTrackers.getCalendar().getDate().getSeason() == Season.SUMMER ? false : super.isBlockHighHumidity(x, y, z);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        Weather weather = HFTrackers.getCalendar().getTodaysWeather();
        if (weather == Weather.SNOW || weather == Weather.BLIZZARD) {
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
    public void resetRainAndThunder() {}

    @Override
    public void updateWeather() {
        if (!worldObj.isRemote) {
            joshie.harvest.calendar.Calendar calendar = HFTrackers.getCalendar();
            float rainStrength = calendar.getTodaysRainStrength();
            float thunderStrength = calendar.getTodaysStormStrength();
            if (calendar.getTodaysWeather().isRain()) {
                HFTrackers.getCropTracker().doRain();
            }
            
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
            
            /*
            if (weather != Weather.SUNNY) {
                
            }
            
            if (weather == Weather.SUNNY) {
                if (worldObj.rainingStrength > 0F) {
                    worldObj.rainingStrength = worldObj.rainingStrength - 0.01F;
                }
            } else if (weather == Weather.DRIZZLE) {
                if (worldObj.rainingStrength < 0.21F) {
                    worldObj.rainingStrength = worldObj.rainingStrength + 0.01F;
                } else if (worldObj.rainingStrength > 0.21F) {
                    worldObj.rainingStrength = worldObj.rainingStrength - 0.01F;
                }
            } else if (weather == Weather.RAIN || weather == Weather.SNOW) {
                if (worldObj.rainingStrength < 1F) {
                    worldObj.rainingStrength = worldObj.rainingStrength + 0.01F;
                } else if (worldObj.rainingStrength > 1F) {
                    worldObj.rainingStrength = worldObj.rainingStrength - 0.01F;
                }
            } else if (weather == Weather.TYPHOON || weather == Weather.BLIZZARD) {
                if (worldObj.rainingStrength < 2F) {
                    worldObj.rainingStrength = worldObj.rainingStrength + 0.01F;
                }
            } 

            if (weather == Weather.TYPHOON) {
                if (worldObj.thunderingStrength < 1F) {
                    worldObj.thunderingStrength = worldObj.thunderingStrength + 0.01F;
                }
            } else if (worldObj.thunderingStrength > 0F) {
                worldObj.thunderingStrength = worldObj.thunderingStrength - 0.01F;
            } */
        }
    }
}
