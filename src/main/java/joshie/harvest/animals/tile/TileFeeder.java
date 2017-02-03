package joshie.harvest.animals.tile;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.base.tile.TileFillable;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.api.animals.AnimalFoodType.SEED;

public class TileFeeder extends TileFillable {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.PRE, Phases.MAIN) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFAnimals.TRAY && HFAnimals.TRAY.getEnumFromState(state).isFeeder();
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileFeeder feeder = (TileFeeder) world.getTileEntity(pos);
            for (EntityAnimal animal : EntityHelper.getEntities(EntityAnimal.class, world, pos, 32D, 5D)) {
                AnimalStats stats = EntityHelper.getStats(animal);
                if (stats != null && feeder.fillAmount > 0 && HFApi.animals.canAnimalEatFoodType(stats, SEED) &&
                        !stats.performTest(AnimalTest.HAS_EATEN) && feeder.setFilled(feeder.getFillAmount() - 1)) {
                    stats.performAction(world, null, null, AnimalAction.FEED);
                }
            }
        }
    };

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }

    @Override
    protected int getMaximumFill() {
        return 100;
    }

    @Override
    public boolean onActivated(ItemStack held) {
        if (held != null && HFApi.animals.canEat(held, SEED)) {
            boolean processed = false;
            for (int i = 0; i < 10 && held.stackSize > 0; i++) {
                if (held.stackSize >= 1) {
                    if (fillAmount < getMaximumFill() && setFilled(getFillAmount() + 10)) {
                        held.splitStack(1);
                        processed = true;
                    } else break;
                }
            }

            return processed;
        }

        return false;
    }
}