package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.util.math.BlockPos;

public class TaskHeadToBlock extends AbstractTask {
    public BlockPos go;

    @Override
    public boolean shouldTerminate(EntityNPC entity) {
        return go == null || entity.getDistanceSq(go) < 5D;
    }

    @Override
    public boolean shouldExecute(EntityNPC entity) {
        return true;
    }

    @Override
    public void execute(EntityNPC entity) {
        entity.getNavigator().tryMoveToXYZ((double) go.getX() + 0.5D, (double) go.getY(), (double) go.getZ() + 0.5D, 0.75D);
    }

    public AbstractTask setLocation(BlockPos go) {
        this.go = go;
        return this;
    }
}
