package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.util.math.BlockPos;

public class TaskGoToShop extends AbstractTask {
    public long attemptTimer;

    @Override
    public boolean shouldTerminate(EntityNPC entity) {
        BlockPos work = NPCHelper.getWorkForEntity(entity);
        if (work == null) return true;
        return !entity.getNPC().getShop().isOpen(entity.worldObj, null);
    }

    @Override
    public boolean shouldExecute(EntityNPC entity) {
        return entity.getNPC().getShop().isPreparingToOpen(entity.worldObj) || entity.getNPC().getShop().isOpen(entity.worldObj, null);
    }

    @Override
    public void execute(EntityNPC entity) {
        //Travel to work, if at work, stay at the work position
        BlockPos work = NPCHelper.getWorkForEntity(entity);
        if (work != null) {
            if (entity.getDistanceSq(work) > 1D) {
                if (attemptTimer < 1000L) {
                    entity.getNavigator().tryMoveToXYZ((double) work.getX() + 0.5D, (double) work.getY(), (double) work.getZ() + 0.5D, 1.0D);
                } else entity.setPositionAndUpdate(work.getX() + 0.5D, work.getY(), work.getZ() + 0.5D);

                attemptTimer++;
            }
        }
    }
}
