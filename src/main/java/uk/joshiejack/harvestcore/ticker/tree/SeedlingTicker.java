package uk.joshiejack.harvestcore.ticker.tree;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;

import java.util.List;

public class SeedlingTicker extends AbstractTreeTicker implements INBTSerializable<NBTTagCompound> {
    protected final IBlockState next;
    protected final int days;

    public SeedlingTicker(String type, IBlockState next, int days) {
        super(type);
        this.next = next;
        this.days = days;
    }

    @Override
    public DailyTicker newInstance() {
        return new SeedlingTicker(getType(), next, days);
    }

    private boolean isNotWinter(World world, BlockPos pos) {
        List<Season> seasons = SeasonsSavedData.getWorldData(world).getSeasonAt(world, pos, AbstractWorldData.CheckMoreThanBiome.YES);
        return seasons.size() == 1 && !seasons.contains(Season.WINTER) || seasons.size() > 1;
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        if (isNotWinter(world, pos)) {
            super.tick(world, pos, state);
            if (age >= days) {
                grow(world, pos);
            }
        }
    }

    protected void grow(World world, BlockPos pos) {
        world.setBlockState(pos, next); //Soooooooooooooooooooooooooooooooooooooooooooooooo oooooooooooooooooooooooo let's get to the same length
        next.getBlock().onBlockPlacedBy(world, pos, next, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos), ItemStack.EMPTY); //Just cause
    }
}
