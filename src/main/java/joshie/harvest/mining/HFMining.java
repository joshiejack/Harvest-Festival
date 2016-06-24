package joshie.harvest.mining;

import joshie.harvest.mining.items.ItemOre;
import joshie.harvest.mining.loot.*;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

public class HFMining {
    public static final ItemOre ORE = new ItemOre().setUnlocalizedName("ore");

    public static void preInit() {
        LootConditionManager.registerCondition(new Between.Serializer());
        LootConditionManager.registerCondition(new EndsIn.Serializer());
        LootConditionManager.registerCondition(new Exact.Serializer());
        LootConditionManager.registerCondition(new MultipleOf.Serializer());
        LootConditionManager.registerCondition(new Obtained.Serializer());

        /*
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HFCrops.HOE, 1, ToolTier.CURSED.ordinal()), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HFCrops.WATERING_CAN, 1, ToolTier.CURSED.ordinal()), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HFCrops.SICKLE, 1, ToolTier.CURSED.ordinal()), 1D)); */
    }
}