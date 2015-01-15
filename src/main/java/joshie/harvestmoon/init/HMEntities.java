package joshie.harvestmoon.init;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.entities.EntityNPC;
import cpw.mods.fml.common.registry.EntityRegistry;

public class HMEntities {
    public static void init() {
        EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 0, HarvestMoon.instance, 80, 3, true);
    }
}
