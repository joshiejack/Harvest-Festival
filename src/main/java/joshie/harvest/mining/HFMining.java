package joshie.harvest.mining;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.BlockDirt;
import joshie.harvest.mining.blocks.BlockLadder;
import joshie.harvest.mining.blocks.BlockStone;
import joshie.harvest.mining.items.ItemOre;
import joshie.harvest.mining.loot.*;
import net.minecraft.world.DimensionType;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.DimensionManager;

import static joshie.harvest.core.config.General.MINING_ID;

public class HFMining {
    public static final DimensionType MINE_WORLD = DimensionType.register("The Mine", "_hf_mine", MINING_ID, MiningProvider.class, false);
    public static final ItemOre ORE = new ItemOre().setUnlocalizedName("ore");

    public static final BlockStone STONE = new BlockStone().setUnlocalizedName("stone");
    public static final BlockDirt DIRT = new BlockDirt().setUnlocalizedName("dirt");
    public static final BlockLadder LADDER = new BlockLadder().setUnlocalizedName("ladder");

    public static void preInit() {
        DimensionManager.registerDimension(MINING_ID, MINE_WORLD);
        LootConditionManager.registerCondition(new Between.Serializer());
        LootConditionManager.registerCondition(new EndsIn.Serializer());
        LootConditionManager.registerCondition(new Exact.Serializer());
        LootConditionManager.registerCondition(new MultipleOf.Serializer());
        LootConditionManager.registerCondition(new Obtained.Serializer());
        HFApi.tickable.registerDailyTickableBlock(DIRT, new MiningTicker());
    }
}