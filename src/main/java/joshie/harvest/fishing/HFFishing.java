package joshie.harvest.fishing;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.lib.EntityIDs;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.fishing.condition.ConditionTime;
import joshie.harvest.fishing.condition.ConditionLocation;
import joshie.harvest.fishing.condition.ConditionSeason;
import joshie.harvest.fishing.entity.EntityFishHookHF;
import joshie.harvest.fishing.item.ItemFish;
import joshie.harvest.fishing.item.ItemFishingRod;
import joshie.harvest.fishing.item.ItemJunk;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@HFLoader
public class HFFishing {
    public static final ItemFish FISH = new ItemFish().register("fish");
    public static final ItemJunk JUNK = new ItemJunk().register("junk");
    public static final ItemFishingRod FISHING_ROD = new ItemFishingRod().register("fishing_rod");

    public static void preInit(){
        LootConditionManager.registerCondition(new ConditionTime.Serializer());
        LootConditionManager.registerCondition(new ConditionLocation.Serializer());
        LootConditionManager.registerCondition(new ConditionSeason.Serializer());
        EntityRegistry.registerModEntity(EntityFishHookHF.class, "hook", EntityIDs.FISHING, HarvestFestival.instance, 64, 5, true);
        EntityRegistry.instance().lookupModSpawn(EntityFishHookHF.class, false).setCustomSpawning(null, true);
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(FISHING_ROD);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() throws Exception {
        RenderingRegistry.registerEntityRenderingHandler(EntityFishHookHF.class, RenderFish::new);
    }
}
