package uk.joshiejack.husbandry.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.tile.TileFeeder;

import javax.annotation.Nonnull;

public class EntityAIEatBirdFeed extends EntityAITimerBase {
    public EntityAIEatBirdFeed(EntityCreature entity, AnimalStats<?> stats) {
        super(entity, stats, Orientation.ABOVE, 8);
    }

    @Override
    public boolean shouldExecute() {
        return !stats.hasEaten() && entity.getRNG().nextInt(5) == 0 && super.shouldExecute();
    }

    @Override
    protected boolean shouldMoveTo(World world, @Nonnull BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileFeeder) {
            BlockTray.Tray tray = ((TileFeeder)tile).getTrayState();
            return tray == BlockTray.Tray.FEEDER_SEMI || tray == BlockTray.Tray.FEEDER_FULL;
        } else return false;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1,
                (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) entity.getVerticalFaceSpeed());
        if (isNearDestination()) {
            TileEntity tile = entity.world.getTileEntity(destinationBlock);
            if (tile instanceof TileFeeder) {
                TileFeeder feeder = (TileFeeder) tile;
                if (feeder.getTrayState() != BlockTray.Tray.FEEDER_EMPTY) {
                    feeder.consume();
                    stats.feed();
                    entity.playLivingSound();
                    timeoutCounter = 9999;
                }
            }
        }
    }
}
