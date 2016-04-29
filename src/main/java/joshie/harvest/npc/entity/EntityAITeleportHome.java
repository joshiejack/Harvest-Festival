package joshie.harvest.npc.entity;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NPCHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAITeleportHome extends EntityAIBase {
    private WorldLocation home;
    private EntityNPC entity;

    public EntityAITeleportHome(EntityNPC npc) {
        entity = npc;
        home = NPCHelper.getHomeForEntity(entity);
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (home == null || this.entity.lastTeleport != 0) return false;
        int bedTime = CalendarHelper.getScaledTime(entity.getNPC().getBedtime());
        long time = CalendarHelper.getTime(entity.worldObj);
        if (time >= bedTime) {
            return (entity.worldObj.canBlockSeeSky(new BlockPos(entity)));
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return entity.lastTeleport == 0;
    }

    @Override
    public void startExecuting() {
        this.entity.setPositionAndUpdate(home.position.getX() + 0.5D, home.position.getY(), home.position.getZ() + 0.5D);
    }

    @Override
    public void resetTask() {
        this.entity.lastTeleport = 1000;
    }
}