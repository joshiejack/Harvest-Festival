package joshie.harvest.animals.tile;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalFoodType;
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
                    stats.performAction(world, ItemStack.EMPTY, AnimalAction.FEED);
                }
            }
        }
    };

    public TileFeeder() {
        super(AnimalFoodType.SEED, 100, 10);
    }

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }
}