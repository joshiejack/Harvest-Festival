package joshie.harvest.mining;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.items.HFItems;
import joshie.harvest.mining.loot.LootCursed;
import joshie.harvest.mining.loot.LootMythic;
import joshie.harvest.mining.loot.MiningLoot;
import net.minecraft.item.ItemStack;

import static joshie.harvest.blocks.BlockDirt.FloorType.*;
import static joshie.harvest.items.ItemGeneral.*;

public class HFMining {
    public static void preInit() {
        MiningLoot.registerLoot(ALL_FLOORS, new ItemStack(HFItems.general, 1, JUNK_ORE), 30D);
        MiningLoot.registerLoot(ALL_FLOORS, new ItemStack(HFItems.general, 1, COPPER_ORE), 15D);
        MiningLoot.registerLoot(ALL_FLOORS, new ItemStack(HFItems.general, 1, SILVER_ORE), 7.5D);
        MiningLoot.registerLoot(GOLD_FLOOR, new ItemStack(HFItems.general, 1, GOLD_ORE), 5D);
        MiningLoot.registerLoot(MYSTRIL_FLOOR, new ItemStack(HFItems.general, 1, MYSTRIL_ORE), 1D);
        MiningLoot.registerLoot(MYTHIC_FLOOR, new LootMythic(new ItemStack(HFItems.general, 1, MYTHIC_STONE), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HFItems.hoe, 1, ToolTier.CURSED.ordinal()), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HFItems.wateringcan, 1, ToolTier.CURSED.ordinal()), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HFItems.sickle, 1, ToolTier.CURSED.ordinal()), 1D));
    }
}
