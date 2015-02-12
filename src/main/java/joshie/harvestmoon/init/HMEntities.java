package joshie.harvestmoon.init;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.EntityNPCBuilder;
import joshie.harvestmoon.npc.EntityNPCMiner;
import joshie.harvestmoon.npc.EntityNPCShopkeeper;
import joshie.harvestmoon.util.generic.EntityFakeItem;
import cpw.mods.fml.common.registry.EntityRegistry;

public class HMEntities {
    public static void init() {
        EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityFakeItem.class, "FakeItem", 1, HarvestMoon.instance, 80, 3, false);
        EntityRegistry.registerModEntity(EntityNPCBuilder.class, "NPCBuilder", 2, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCShopkeeper.class, "NPCShopkeeper", 2, HarvestMoon.instance, 80, 3, true);
        EntityRegistry.registerModEntity(EntityNPCMiner.class, "NPCMiner", 2, HarvestMoon.instance, 80, 3, true);
    }
}
