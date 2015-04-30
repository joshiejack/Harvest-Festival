package joshie.harvestmoon.init;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.animals.EntityHarvestCow;
import joshie.harvestmoon.core.util.generic.EntityFakeItem;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.EntityNPCBuilder;
import joshie.harvestmoon.npc.EntityNPCMiner;
import joshie.harvestmoon.npc.EntityNPCShopkeeper;
import cpw.mods.fml.common.registry.EntityRegistry;

public class HMEntities {
    public static void init() {
        EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityFakeItem.class, "FakeItem", 1, HarvestMoon.instance, 80, 3, false);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "NPCBuilder", 2, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "NPCShopkeeper", 3, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCMiner.class, "NPCMiner", 4, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityHarvestCow.class, "HarvestCow", 5, HarvestMoon.instance, 150, 3, true);
    }
}
