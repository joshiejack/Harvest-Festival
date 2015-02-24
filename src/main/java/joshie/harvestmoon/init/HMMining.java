package joshie.harvestmoon.init;

import static joshie.harvestmoon.blocks.BlockDirt.FloorType.ALL_FLOORS;
import static joshie.harvestmoon.blocks.BlockDirt.FloorType.CURSED_FLOOR;
import static joshie.harvestmoon.blocks.BlockDirt.FloorType.GOLD_FLOOR;
import static joshie.harvestmoon.blocks.BlockDirt.FloorType.MYSTRIL_FLOOR;
import static joshie.harvestmoon.blocks.BlockDirt.FloorType.MYTHIC_FLOOR;
import static joshie.harvestmoon.items.ItemGeneral.COPPER_ORE;
import static joshie.harvestmoon.items.ItemGeneral.GOLD_ORE;
import static joshie.harvestmoon.items.ItemGeneral.JUNK_ORE;
import static joshie.harvestmoon.items.ItemGeneral.MYSTRIL_ORE;
import static joshie.harvestmoon.items.ItemGeneral.MYTHIC_STONE;
import static joshie.harvestmoon.items.ItemGeneral.SILVER_ORE;
import joshie.harvestmoon.items.ItemBaseTool.ToolTier;
import joshie.harvestmoon.mining.LootCursed;
import joshie.harvestmoon.mining.LootMythic;
import joshie.harvestmoon.mining.MiningLoot;
import net.minecraft.item.ItemStack;

public class HMMining {
    public static void init() {
        MiningLoot.registerLoot(ALL_FLOORS, new ItemStack(HMItems.general, 1, JUNK_ORE), 30D);
        MiningLoot.registerLoot(ALL_FLOORS, new ItemStack(HMItems.general, 1, COPPER_ORE), 15D);
        MiningLoot.registerLoot(ALL_FLOORS, new ItemStack(HMItems.general, 1, SILVER_ORE), 7.5D);
        MiningLoot.registerLoot(GOLD_FLOOR, new ItemStack(HMItems.general, 1, GOLD_ORE), 5D);
        MiningLoot.registerLoot(MYSTRIL_FLOOR, new ItemStack(HMItems.general, 1, MYSTRIL_ORE), 1D);
        MiningLoot.registerLoot(MYTHIC_FLOOR, new LootMythic(new ItemStack(HMItems.general, 1, MYTHIC_STONE), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HMItems.hoe, 1, ToolTier.CURSED.ordinal()), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HMItems.wateringcan, 1, ToolTier.CURSED.ordinal()), 1D));
        MiningLoot.registerLoot(CURSED_FLOOR, new LootCursed(new ItemStack(HMItems.sickle, 1, ToolTier.CURSED.ordinal()), 1D));
    }
}
