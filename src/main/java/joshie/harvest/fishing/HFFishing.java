package joshie.harvest.fishing;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.fishing.FishingHelper.WaterType;
import joshie.harvest.fishing.block.BlockFishing;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.fishing.item.ItemFish;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.fishing.item.ItemFishingRod;
import joshie.harvest.fishing.item.ItemJunk;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import joshie.harvest.fishing.loot.ConditionTier;
import joshie.harvest.fishing.loot.ConditionTime;
import joshie.harvest.fishing.loot.SetWeight;
import joshie.harvest.fishing.render.SpecialRendererHatchery;
import joshie.harvest.fishing.render.SpecialRendererTrap;
import joshie.harvest.fishing.tile.TileHatchery;
import joshie.harvest.fishing.tile.TileTrap;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood.FishType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.helpers.RegistryHelper.registerOreIfNotExists;
import static joshie.harvest.core.helpers.RegistryHelper.registerTiles;
import static joshie.harvest.fishing.FishingHelper.WaterType.*;
import static net.minecraft.block.BlockLiquid.LEVEL;

@HFLoader
public class HFFishing {
    public static final StateMap NO_WATER = new StateMap.Builder().ignore(LEVEL).build();
    public static final ItemFish FISH = new ItemFish().register("fish");
    public static final ItemJunk JUNK = new ItemJunk().register("junk");
    public static final ItemFishingRod FISHING_ROD = new ItemFishingRod().register("fishing_rod");
    public static final BlockFishing FISHING_BLOCK = new BlockFishing().register("fishing_block");

    @SuppressWarnings("unchecked")
    public static void preInit(){
        LootFunctionManager.registerFunction(new SetWeight.Serializer());
        LootConditionManager.registerCondition(new ConditionTime.Serializer());
        LootConditionManager.registerCondition(new ConditionTier.Serializer());
        EntityRegistry.registerModEntity(EntityFishHookHF.class, "hook", EntityIDs.FISHING, HarvestFestival.instance, 64, 5, true);
        EntityRegistry.instance().lookupModSpawn(EntityFishHookHF.class, false).setCustomSpawning(null, true);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 0), 10L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 1), 30L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 2), 50L);
        HFApi.shipping.registerSellable(new ItemStack(Items.FISH, 1, 3), 100L);
        HFApi.fishing.registerBait(JUNK.getStackFromEnum(Junk.BAIT));
        registerTiles(TileTrap.class, TileHatchery.class);
        FishingAPI.INSTANCE.breeding.register(Ore.of("fish"), 3);
        //Register vanilla fish
        for (FishType fish: FishType.values()) {
            registerOreIfNotExists("fish", new ItemStack(Items.FISH, 1, fish.getMetadata()));
        }

        //Register my fish
        for (Fish fish: Fish.values()) {
            registerOreIfNotExists("fish", FISH.getStackFromEnum(fish));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() throws Exception {
        RenderingRegistry.registerEntityRenderingHandler(EntityFishHookHF.class, RenderFish::new);
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(FISHING_ROD);
        registerLootTable("lake_spring", LAKE, SPRING);
        registerLootTable("lake_summer", LAKE, SUMMER);
        registerLootTable("lake_autumn", LAKE, AUTUMN);
        registerLootTable("lake_winter", LAKE, WINTER);
        registerLootTable("ocean_spring", OCEAN, SPRING);
        registerLootTable("ocean_summer", OCEAN, SUMMER);
        registerLootTable("ocean_autumn", OCEAN, AUTUMN);
        registerLootTable("ocean_winter", OCEAN, WINTER);
        registerLootTable("pond_spring", POND, SPRING);
        registerLootTable("pond_summer", POND, SUMMER);
        registerLootTable("pond_autumn", POND, AUTUMN);
        registerLootTable("pond_winter", POND, WINTER);
        registerLootTable("river_spring", RIVER, SPRING);
        registerLootTable("river_summer", RIVER, SUMMER);
        registerLootTable("river_autumn", RIVER, AUTUMN);
        registerLootTable("river_winter", RIVER, WINTER);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileHatchery.class, new SpecialRendererHatchery());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTrap.class, new SpecialRendererTrap());
    }

    private static void registerLootTable(String id, WaterType type, Season season) {
        FishingHelper.FISHING_LOOT.put(Pair.of(season, type), LootTableList.register(new ResourceLocation(HFModInfo.MODID, "gameplay/fishing/" + id)));
    }
}
