package uk.joshiejack.seasons.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.client.util.SkyBlender;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.SeasonsConfig;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.data.SeasonData;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import uk.joshiejack.seasons.world.weather.Weather;

import javax.annotation.Nonnull;

public class SeasonsWorldProvider extends WorldProviderSurface {
    private double targetRain;
    private double targetThunder;

    public static AbstractWorldData getWorldData(World world) {
        return world.isRemote ? getWorldClientData() : SeasonsSavedData.getWorldData(world);
    }

    @SideOnly(Side.CLIENT)
    private static AbstractWorldData getWorldClientData() {
        return WorldDataClient.INSTANCE;
    }

    @Nonnull
    @Override
    public Vec3d getSkyColor(@Nonnull net.minecraft.entity.Entity entity, float partialTicks) {
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.posY);
        int k = MathHelper.floor(entity.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Season season = getWorldData(world).fromBiome(world.getBiome(blockpos));
        SeasonData data = SeasonData.DATA.get(season);
        if (data == null) return super.getSkyColor(entity, partialTicks);
        float f = world.getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        int l = SkyBlender.get(world, blockpos, SeasonData.DATA.get(getWorldData(world).fromBiome(world.getBiome(blockpos))).sky);
        float f3 = (float) (l >> 16 & 255) / 255.0F;
        float f4 = (float) (l >> 8 & 255) / 255.0F;
        float f5 = (float) (l & 255) / 255.0F;
        f3 = f3 * f1;
        f4 = f4 * f1;
        f5 = f5 * f1;
        float f6 = world.getRainStrength(partialTicks);

        if (f6 > 0.0F) {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f8 = 1.0F - f6 * 0.75F;
            f3 = f3 * f8 + f7 * (1.0F - f8);
            f4 = f4 * f8 + f7 * (1.0F - f8);
            f5 = f5 * f8 + f7 * (1.0F - f8);
        }

        float f10 = world.getThunderStrength(partialTicks);

        if (f10 > 0.0F) {
            float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f9 = 1.0F - f10 * 0.75F;
            f3 = f3 * f9 + f11 * (1.0F - f9);
            f4 = f4 * f9 + f11 * (1.0F - f9);
            f5 = f5 * f9 + f11 * (1.0F - f9);
        }

        if (world.getLastLightningBolt() > 0) {
            float f12 = (float) world.getLastLightningBolt() - partialTicks;

            if (f12 > 1.0F) {
                f12 = 1.0F;
            }

            f12 = f12 * 0.45F;
            f3 = f3 * (1.0F - f12) + 0.8F * f12;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 1.0F * f12;
        }

        return new Vec3d(f3, f4, f5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        return getWorldData(world).getSeason() == Season.WINTER
                ? super.getStarBrightness(par1) * 1.25F : super.getStarBrightness(par1);
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        if (world == null || (!world.isRemote && world.getMapStorage() == null)) return super.calculateCelestialAngle(worldTime, partialTicks);
        Season season = world.isRemote ? WorldDataClient.INSTANCE.getSeason() : SeasonsSavedData.getWorldData(world).getSeason();
        SeasonData data = SeasonData.DATA.get(season);
        if (data == null) return super.calculateCelestialAngle(worldTime, partialTicks);
        long time = TimeHelper.getTimeOfDay(world.getWorldTime());
        if (time >= data.sunrise && time < data.sunset) {
            long daytime = (data.sunset - data.sunrise);
            return (((time - data.sunrise) * -0.5f) / daytime) - 0.75f;
        } else {
            if (time < data.sunrise) time += 24000L; //Adjust the time so that we're the day after
            long daytime = ((24000L + data.sunrise) - data.sunset);
            return (((time - data.sunset) * -0.5f) / daytime) - 0.25f;
        }
    }

    private boolean isInit;

    //Call this each time the day changes
    @Override
    public void calculateInitialWeather() {
        if (SeasonsConfig.dailyWeather) {
            if (world == null || (!world.isRemote && world.getMapStorage() == null)) super.calculateInitialWeather();
            else {
                isInit = true; //Yes we do this here
                Weather weather = getWorldData(world).getWeather();
                switch (weather) {
                    case CLEAR:
                    case FOGGY:
                        targetRain = 0F;
                        targetThunder = 0F;
                        break;
                    case RAIN:
                        targetRain = 1F;
                        targetThunder = 0F;
                        break;
                    case STORM:
                        targetRain = 2F;
                        targetThunder = 1F;
                        break;
                }
            }
        } else super.calculateInitialWeather();
    }

    @Override
    public void updateWeather() {
        if (SeasonsConfig.dailyWeather) {
            if (!isInit) calculateInitialWeather();
            if (world.rainingStrength < targetRain) {
                world.rainingStrength += 0.005F;
            } else if (world.rainingStrength > targetRain) {
                world.rainingStrength -= 0.005F;
            }

            if (world.thunderingStrength < targetThunder) {
                world.thunderingStrength += 0.005F;
            } else if (world.rainingStrength > targetThunder) {
                world.thunderingStrength -= 0.005F;
            }
        } else super.updateWeather();
    }
}
