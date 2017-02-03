package joshie.harvest.animals.tile;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.block.BlockTrough;
import joshie.harvest.animals.block.BlockTrough.Section;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.base.tile.TileFillableConnected;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.api.animals.AnimalFoodType.GRASS;

public class TileTrough extends TileFillableConnected<TileTrough> {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.PRE, Phases.MAIN) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFAnimals.TROUGH;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileTrough trough = (TileTrough) world.getTileEntity(pos);
            if (trough.getMaster() == trough) {
                for (EntityAnimal animal : EntityHelper.getEntities(EntityAnimal.class, world, pos, 32D, 5D)) {
                    AnimalStats stats = EntityHelper.getStats(animal);
                    if (stats != null && trough.fillAmount > 0 && HFApi.animals.canAnimalEatFoodType(stats, GRASS) &&
                            !stats.performTest(AnimalTest.HAS_EATEN) && trough.setFilled(trough.getFillAmount() - 1)) {
                        stats.performAction(world, null, null, AnimalAction.FEED);
                    }
                }
            }
        }
    };

    private Section section;
    private EnumFacing facing;

    public TileTrough() {
        super(3);
    }

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }

    @Override
    public boolean onActivated(ItemStack held) {
        if (HFApi.animals.canEat(held, GRASS)) {
            TileTrough master = getMaster();
            if (master != null) {
                if (held.stackSize >= 1) {
                    if (master.fillAmount < master.getMaximumFill() && master.setFilled(master.getFillAmount() + 10)) {
                        held.splitStack(1);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected boolean isValidConnection(BlockPos pos) {
        return worldObj.getTileEntity(pos) instanceof TileTrough;
    }

    @Override
    public void resetClientData() {
        section = null;
        facing = null;
    }

    public Section getSection() {
        if (section != null) return section;
        else {
            IBlockState state = worldObj.getBlockState(pos);
            IBlockState actualState = state.getActualState(worldObj, pos);
            section = actualState.getValue(BlockTrough.SECTION);
        }

        return section;
    }

    public EnumFacing getFacing() {
        if (facing != null) return facing;
        else {
            IBlockState state = worldObj.getBlockState(pos);
            IBlockState actualState = state.getActualState(worldObj, pos);
            facing = actualState.getValue(BlockTrough.FACING);
        }

        return facing;
    }
}