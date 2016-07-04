package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.util.math.BlockPos;

public class TaskHeadToBlock extends AbstractTask {
    public BlockPos go;

    @Override
    public boolean shouldTerminate(AbstractEntityNPC entity) {
        return go == null || entity.getDistanceSq(go) < 5D;
    }

    @Override
    public boolean shouldExecute(AbstractEntityNPC entity) {
        return true;
    }

    @Override
    public boolean demandOnly() {
        return true;
    }

    @Override
    public void execute(AbstractEntityNPC entity) {
        entity.getNavigator().tryMoveToXYZ((double) go.getX() + 0.5D, (double) go.getY(), (double) go.getZ() + 0.5D, 1.0D);
    }

    public AbstractTask setLocation(BlockPos go) {
        this.go = go;
        return this;
    }
}
