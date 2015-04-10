package joshie.harvestmoon.npc.ai;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.NPCHelper;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;

public class EntityAITeleportHome extends EntityAIBase {
    private WorldLocation home;
    private EntityNPC entity;
    private int insidePosX = -1;
    private int insidePosZ = -1;
    private int insidePosY = -1;

    public EntityAITeleportHome(EntityNPC npc) {
        entity = npc;
        home = NPCHelper.getHomeForEntity(entity);
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (home == null || this.entity.lastTeleport != 0) return false;
        int i = MathHelper.floor_double(this.entity.posX);
        int j = MathHelper.floor_double(this.entity.posY);
        int k = MathHelper.floor_double(this.entity.posZ);
        int lastTeleport = this.entity.lastTeleport;
        int bedTime = CalendarHelper.getScaledTime(entity.getNPC().getBedtime());
        long time = CalendarHelper.getTime(entity.worldObj);
        if (time >= bedTime) {
            return (entity.worldObj.canBlockSeeTheSky(i, j, k));
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return entity.lastTeleport == 0;
    }

    @Override
    public void startExecuting() {
        this.entity.setPositionAndUpdate(home.x + 0.5D, home.y, home.z + 0.5D);
    }

    @Override
    public void resetTask() {
        this.entity.lastTeleport = 1000;
    }
}