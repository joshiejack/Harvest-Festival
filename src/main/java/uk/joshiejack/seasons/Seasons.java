package uk.joshiejack.seasons;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.util.interfaces.DateFormatter;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.client.renderer.FogRenderer;
import uk.joshiejack.seasons.data.SeasonData;
import uk.joshiejack.seasons.date.CalendarDate;
import uk.joshiejack.seasons.handlers.SeasonalCrops;
import uk.joshiejack.seasons.world.SeasonsWorldProvider;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.storage.loot.condition.ConditionSeason;

import java.util.Random;

@Mod.EventBusSubscriber
@Mod(modid = Seasons.MODID, name = "Seasons", version = "@SEASONS_VERSION@", dependencies = "required-after:penguinlib")
public class Seasons {
    public static final String MODID = "seasons";
    private static final NoiseGeneratorPerlin TEMPERATURE_NOISE = new NoiseGeneratorPerlin(new Random(1234L), 1);
    public static final int DAYS_PER_SEASON = 28;
    public static Season SEASON = Season.SPRING;

    @SidedProxy
    public static ServerProxy proxy;

    public static class ServerProxy { public void preInit() {} }
    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends ServerProxy {
        @Override
        public void preInit() {
            MinecraftForge.EVENT_BUS.register(FogRenderer.class);
            try {
                Class.forName("uk.joshiejack.harvestcore.client.gui.label.LabelTownInfo").getField("formatter").set(null, (DateFormatter) time -> {
                    CalendarDate date = CalendarDate.getFromTime(time);
                    return Season.fromTime(time).name() + " " + date.getDay() + ", Year " + date.getYear();
                });
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignored) {}
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        LootConditionManager.registerCondition(new ConditionSeason.Serializer());
        DimensionType seasons = DimensionType.register("seasons", "seasons", SeasonsConfig.overworldID, SeasonsWorldProvider.class, true);
        DimensionManager.unregisterDimension(0);
        DimensionManager.registerDimension(0, seasons);
        if (SeasonsConfig.seasonalCropGrowth) {
            MinecraftForge.EVENT_BUS.register(SeasonalCrops.class);
        }
    }

    @SideOnly(Side.CLIENT)
    public static Season getSeasonClient() {
        return WorldDataClient.INSTANCE.getSeason();
    }

    @SuppressWarnings("unused")
    public static float getTemperature(BlockPos pos, Biome biome) {
        Season season = AbstractWorldData.fromBiomeOr(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? getSeasonClient() : SEASON, biome);
        SeasonData data = SeasonData.DATA.get(season);
        float temp = (data == null ? 0 : data.temperature) + biome.getDefaultTemperature();
        if (pos.getY() > 64) {
            float f = (float) (TEMPERATURE_NOISE.getValue((float) pos.getX() / 8.0F, (float) pos.getZ() / 8.0F) * 4.0D);
            return temp - (f + (float) pos.getY() - 64.0F) * 0.05F / 30.0F;
        } else return temp;
    }
}
