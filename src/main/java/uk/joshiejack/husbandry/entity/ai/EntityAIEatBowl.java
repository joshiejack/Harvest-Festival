package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.tile.TileBowl;
import net.minecraft.entity.EntityCreature;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAIEatBowl extends EntityAITimerBase {
    private final BlockTray.Tray type;

    public EntityAIEatBowl(EntityCreature entity, AnimalStats<?> stats, BlockTray.Tray type) {
        super(entity, stats, Orientation.BESIDE, 8);
        this.type = type;
    }

    @Override
    public boolean shouldExecute() {
        return !stats.hasEaten() && entity.getRNG().nextInt(5) == 0 && super.shouldExecute();
    }

    @Override
    protected boolean shouldMoveTo(World world, @Nonnull BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBowl) {
            return ((TileBowl) tile).getTrayState() == type;
        } else return false;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1,
                (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) entity.getVerticalFaceSpeed());

        if (isNearDestination()) {
            TileEntity tile = entity.world.getTileEntity(destinationBlock);
            if (tile instanceof TileBowl) {
                ((TileBowl) tile).consume();
                stats.feed();
                entity.playLivingSound();
                timeoutCounter = 9999;
            }
        }
    }
}
