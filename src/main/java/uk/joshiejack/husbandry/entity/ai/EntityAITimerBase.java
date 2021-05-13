package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;

public abstract class EntityAITimerBase extends EntityAIMoveToBlock {
    protected final AnimalStats<?> stats;
    protected final EntityCreature entity;
    protected final Orientation orientation;

    public EntityAITimerBase(EntityCreature entity, AnimalStats<?> stats, Orientation orientation, int searchLength) {
        super(entity, 1D, searchLength);
        this.entity = entity;
        this.orientation = orientation;
        this.stats = stats;
    }

    public boolean isNearDestination() {
        return getIsAboveDestination();
    }

    private boolean isNearDestination(BlockPos pos) {
        switch (orientation) {
            case ABOVE:
             return entity.getDistanceSqToCenter(pos) < 1;
            case IN:
                return entity.getDistanceSqToCenter(pos) <= 1.0D;
            case BESIDE:
                return entity.getDistanceSqToCenter(pos) <= 2.5D;
            default:
                return false;
        }
    }

    @Override
    public void updateTask() {
        if (!isNearDestination(destinationBlock)) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0) {
                entity.getNavigator().tryMoveToXYZ((double) ((float) destinationBlock.getX()) + 0.5D,
                        destinationBlock.getY() + 1D, (double) ((float) destinationBlock.getZ()) + 0.5D, 0.8D);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }

    public void resetRunTimer() {
        this.runDelay = 0;
    }

    public enum Orientation {
        ABOVE, IN, BESIDE
    }
}
