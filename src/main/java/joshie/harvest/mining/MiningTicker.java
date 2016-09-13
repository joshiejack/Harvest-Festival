package joshie.harvest.mining;

import joshie.harvest.api.ticking.IDailyTickableBlock;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.HFCore;
import joshie.harvest.mining.block.BlockOre.Ore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static joshie.harvest.mining.HFMining.ORE;

public class MiningTicker implements IDailyTickableBlock {
    public static final int MYSTRIL_FLOOR = 150;
    public static final int GOLD_FLOOR = 80;
    public static final int SILVER_FLOOR = 40;
    private static final int MYSTRIL_CHANCE = MYSTRIL_FLOOR * 20;
    private static final int GOLD_CHANCE = GOLD_FLOOR * 18;
    private static final int SILVER_CHANCE = SILVER_FLOOR * 16;
    private static final int COPPER_CHANCE = 14;

    private static final double WORLD_HEIGHT = 256D;
    static final int MAX_Y = (int) WORLD_HEIGHT - 1;
    static final int FLOOR_HEIGHT = 6;
    static final int MAX_LOOP = (int) WORLD_HEIGHT - FLOOR_HEIGHT;
    static final int MAX_FLOORS = (int) Math.floor(WORLD_HEIGHT/FLOOR_HEIGHT);

    public static IBlockState getBlockState(Random rand, int floor) {
        Ore ore = Ore.ROCK;
        if (rand.nextInt(16) == 0) return HFCore.FLOWERS.getStateFromEnum(FlowerType.WEED);
        if (floor >= MYSTRIL_FLOOR && rand.nextInt(MYSTRIL_CHANCE) <= floor) ore = Ore.MYSTRIL;
        else if (floor >= GOLD_FLOOR && rand.nextInt(GOLD_CHANCE) <= floor) ore = Ore.GOLD;
        else if (floor >= SILVER_FLOOR && rand.nextInt(SILVER_CHANCE) <= floor) ore = Ore.SILVER;
        else if (rand.nextInt(COPPER_CHANCE) == 0) ore = Ore.COPPER;
        return ORE.getStateFromEnum(ore);
    }

    @Override
    public boolean isMiningWorld() {
        return true;
    }

    @Override
    public boolean newDay(World world, BlockPos pos, IBlockState state) {
        BlockPos up = pos.up();
        IBlockState above = world.getBlockState(up);
        if (above.getBlock() == Blocks.AIR) {
            int floor = MiningHelper.getFloor(world.getChunkFromBlockCoords(pos).xPosition, pos.getY());
            if (world.rand.nextInt(32) == 0) world.setBlockState(up, HFCore.FLOWERS.getStateFromEnum(FlowerType.WEED));
            else world.setBlockState(up, getBlockState(world.rand, floor));
        }

        return true;
    }
}
