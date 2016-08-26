package joshie.harvest.npc.entity.ai;

import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TaskSchedule extends AbstractTask {
    public long attemptTimer;

    @Override
    public boolean shouldTerminate(EntityNPC entity) {
        BlockPos home = NPCHelper.getHomeForEntity(entity);
        if (home == null) return true;
        return entity.getDistanceSq(home) < 1D;
    }

    @Override
    public boolean shouldExecute(EntityNPC entity) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
        int bedTime = CalendarHelper.getScaledTime(entity.getNPC().getBedtime() - 1500);
        long time = CalendarHelper.getTime(entity.worldObj);
        if (time >= bedTime) {
            return (entity.worldObj.canBlockSeeSky(mutablePos));
        }

        if ((!entity.worldObj.isDaytime() || entity.worldObj.isRaining() || !entity.worldObj.getBiome(mutablePos).canRain()) && !entity.worldObj.provider.getHasNoSky()) { //If it's raining or night, send the npc work
            //If the entity is not inside
            return (entity.worldObj.canBlockSeeSky(mutablePos)); //If she can see the sky continue executing
        } else return false;
    }

    @Override
    public void execute(EntityNPC entity) {
        BlockPos go = NPCHelper.getHomeForEntity(entity);
        if (go != null) {
            if (attemptTimer < 1000L) {
                entity.getNavigator().tryMoveToXYZ((double) go.getX() + 0.5D, (double) go.getY(), (double) go.getZ() + 0.5D, 1.0D);
            } else entity.setPositionAndUpdate(go.getX() + 0.5D, go.getY(), go.getZ() + 0.5D);

            attemptTimer++;
        }
    }
}
