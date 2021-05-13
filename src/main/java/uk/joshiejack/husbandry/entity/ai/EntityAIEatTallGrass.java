package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.BlockStates;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;

import static uk.joshiejack.husbandry.entity.ai.EntityAITimerBase.Orientation.IN;

public class EntityAIEatTallGrass extends EntityAITimerBase {
    private static final Predicate<IBlockState> IS_TALL_GRASS = BlockStateMatcher.forBlock(Blocks.TALLGRASS).where(BlockTallGrass.TYPE, enumType -> Objects.equals(enumType, BlockTallGrass.EnumType.GRASS));
    private static final Predicate<IBlockState> IS_DOUBLE_TALL_GRASS = BlockStateMatcher.forBlock(Blocks.DOUBLE_PLANT).where(BlockDoublePlant.VARIANT, enumPlantType -> Objects.equals(enumPlantType, BlockDoublePlant.EnumPlantType.GRASS));

    public EntityAIEatTallGrass(EntityCreature entity, AnimalStats<?> stats) {
        super(entity, stats, IN, 16);
    }

    @Override
    public boolean shouldExecute() {
        return !stats.hasEaten() && entity.getRNG().nextInt(50) == 0 && super.shouldExecute();
    }

    @Override
    protected boolean shouldMoveTo(World world, @Nonnull BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return IS_TALL_GRASS.test(state) || IS_DOUBLE_TALL_GRASS.test(state);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1,
                (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) entity.getVerticalFaceSpeed());

        if (isNearDestination()) {
            IBlockState state = entity.world.getBlockState(destinationBlock);
            if (IS_TALL_GRASS.test(state)) {
                if (ForgeEventFactory.getMobGriefingEvent(entity.world, entity)) {
                    entity.world.destroyBlock(destinationBlock, false);
                }

                stats.feed();
                timeoutCounter = 9999;
            } else if (IS_DOUBLE_TALL_GRASS.test(state)) {
                if (ForgeEventFactory.getMobGriefingEvent(entity.world, entity)) {
                    entity.world.destroyBlock(destinationBlock, false);
                    entity.world.setBlockState(destinationBlock, BlockStates.TALL_GRASS);
                }

                stats.feed();
                timeoutCounter = 9999;
            }
        }
    }
}
