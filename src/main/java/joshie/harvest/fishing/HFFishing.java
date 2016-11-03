package joshie.harvest.fishing;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.fishing.FishingHelper.WaterType;
import joshie.harvest.fishing.block.BlockFishing;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.fishing.item.ItemFish;
import joshie.harvest.fishing.item.ItemFishingRod;
import joshie.harvest.fishing.item.ItemJunk;
import joshie.harvest.fishing.loot.ConditionTime;
import joshie.harvest.fishing.loot.SetWeight;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.fishing.FishingHelper.WaterType.*;

@HFLoader
public class HFFishing {
    public static final ItemFish FISH = new ItemFish().register("fish");
    public static final ItemJunk JUNK = new ItemJunk().register("junk");
    public static final ItemFishingRod FISHING_ROD = new ItemFishingRod().register("fishing_rod");
    public static final BlockFishing FISHING_BLOCK = new BlockFishing().register("fishing_block");

    public static void preInit(){
        LootFunctionManager.registerFunction(new SetWeight.Serializer());
        LootConditionManager.registerCondition(new ConditionTime.Serializer());
        EntityRegistry.registerModEntity(EntityFishHookHF.class, "hook", EntityIDs.FISHING, HarvestFestival.instance, 64, 5, true);
        EntityRegistry.instance().lookupModSpawn(EntityFishHookHF.class, false).setCustomSpawning(null, true);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 0), 10L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 1), 30L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 2), 50L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 3), 100L);
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(FISHING_ROD);
        registerLootTable("gameplay/fishing/lake_spring", LAKE, SPRING);
        registerLootTable("gameplay/fishing/lake_summer", LAKE, SUMMER);
        registerLootTable("gameplay/fishing/lake_autumn", LAKE, AUTUMN);
        registerLootTable("gameplay/fishing/lake_winter", LAKE, WINTER);
        registerLootTable("gameplay/fishing/ocean_spring", OCEAN, SPRING);
        registerLootTable("gameplay/fishing/ocean_summer", OCEAN, SUMMER);
        registerLootTable("gameplay/fishing/ocean/autumn", OCEAN, AUTUMN);
        registerLootTable("gameplay/fishing/ocean_winter", OCEAN, WINTER);
        registerLootTable("gameplay/fishing/pond_spring", POND, SPRING);
        registerLootTable("gameplay/fishing/pond_summer", POND, SUMMER);
        registerLootTable("gameplay/fishing/pond_autumn", POND, AUTUMN);
        registerLootTable("gameplay/fishing/pond_winter", POND, WINTER);
        registerLootTable("gameplay/fishing/river_spring", RIVER, SPRING);
        registerLootTable("gameplay/fishing/river_summer", RIVER, SUMMER);
        registerLootTable("gameplay/fishing/river_autumn", RIVER, AUTUMN);
        registerLootTable("gameplay/fishing/river_winter", RIVER, WINTER);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() throws Exception {
        RenderingRegistry.registerEntityRenderingHandler(EntityFishHookHF.class, RenderFish::new);
    }

    private static ResourceLocation registerLootTable(String id, WaterType type, Season season) {
        ResourceLocation resource = LootTableList.register(new ResourceLocation(HFModInfo.MODID, id));
        FishingHelper.LOOT_TABLES.put(Pair.of(season, type), resource);
        return resource;
    }
}
