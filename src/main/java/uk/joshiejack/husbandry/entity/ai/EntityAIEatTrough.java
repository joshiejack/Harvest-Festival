package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.item.ItemFeed;
import uk.joshiejack.husbandry.tile.TileTrough;
import net.minecraft.entity.EntityCreature;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static uk.joshiejack.husbandry.entity.ai.EntityAITimerBase.Orientation.BESIDE;

public class EntityAIEatTrough extends EntityAITimerBase {
    private final ItemFeed.Feed feed;

    public EntityAIEatTrough(EntityCreature entity, AnimalStats<?> stats, ItemFeed.Feed feed) {
        super(entity, stats, BESIDE, 16);
        this.feed = feed;
    }

    @Override
    public boolean shouldExecute() {
        return !stats.hasEaten() && entity.getRNG().nextInt(5) == 0 && super.shouldExecute();
    }

    @Override
    protected boolean shouldMoveTo(World world, @Nonnull BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTrough) {
            return ((TileTrough)tile).getType() == feed;
        } else return false;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1,
                (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) entity.getVerticalFaceSpeed());

        if (isNearDestination()) {
            TileEntity tile = entity.world.getTileEntity(destinationBlock);
            if (tile instanceof TileTrough) {
                ((TileTrough) tile).consume();
                stats.feed();
                entity.playLivingSound();
                timeoutCounter = 9999;
            }
        }
    }
}