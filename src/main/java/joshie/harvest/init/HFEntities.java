package joshie.harvest.init;

import joshie.harvest.HarvestFestival;
import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.core.util.generic.EntityFakeItem;
import joshie.harvest.npc.EntityNPC;
import joshie.harvest.npc.EntityNPCBuilder;
import joshie.harvest.npc.EntityNPCMiner;
import joshie.harvest.npc.EntityNPCShopkeeper;
import cpw.mods.fml.common.registry.EntityRegistry;

public class HFEntities {
    public static void init() {
        EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityFakeItem.class, "FakeItem", 1, HarvestFestival.instance, 80, 3, false);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "NPCBuilder", 2, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "NPCShopkeeper", 3, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCMiner.class, "NPCMiner", 4, HarvestFestival.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityHarvestCow.class, "HarvestCow", 5, HarvestFestival.instance, 150, 3, true);
        EntityRegistry.registerModEntity(EntityHarvestSheep.class, "HarvestSheep", 6, HarvestFestival.instance, 150, 3, true);
    }
}
