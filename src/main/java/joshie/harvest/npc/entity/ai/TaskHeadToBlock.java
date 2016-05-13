package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.npc.ai.INPCTask;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.util.math.BlockPos;

public class TaskHeadToBlock implements INPCTask {
    public BlockPos go;

    @Override
    public boolean shouldTerminate(EntityNPC entity, INPC npc) {
        return go == null || entity.getDistanceSq(go) < 5D;
    }

    @Override
    public boolean shouldExecute(EntityNPC entity, INPC npc) {
        return true;
    }

    @Override
    public boolean demandOnly() {
        return true;
    }

    @Override
    public void execute(EntityNPC entity, INPC npc) {
        entity.getNavigator().tryMoveToXYZ((double) go.getX() + 0.5D, (double) go.getY(), (double) go.getZ() + 0.5D, 1.0D);
    }

    public INPCTask setLocation(BlockPos go) {
        this.go = go;
        return this;
    }
}
