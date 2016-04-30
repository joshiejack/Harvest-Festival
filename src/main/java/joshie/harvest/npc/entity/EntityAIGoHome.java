package joshie.harvest.npc.entity;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NPCHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAIGoHome extends EntityAIBase {
    private WorldLocation home;
    private EntityNPC entity;
    private BlockPos insidePos = new BlockPos(-1, -1, -1);

    public EntityAIGoHome(EntityNPC npc) {
        entity = npc;
        home = NPCHelper.getHomeForEntity(entity);
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (home == null) return false;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(MathHelper.floor_double(this.entity.posX), MathHelper.floor_double(this.entity.posY), MathHelper.floor_double(this.entity.posZ));
        int bedTime = CalendarHelper.getScaledTime(entity.getNPC().getBedtime() - 1500);
        long time = CalendarHelper.getTime(entity.worldObj);
        if (time >= bedTime) {
            return (entity.worldObj.canBlockSeeSky(mutablePos));
        }

        if ((!entity.worldObj.isDaytime() || entity.worldObj.isRaining() || !this.entity.worldObj.getBiomeGenForCoords(mutablePos).canRain()) && !this.entity.worldObj.provider.getHasNoSky()) { //If it's raining or night, send the npc home
            //If the entity is not inside
            return (entity.worldObj.canBlockSeeSky(mutablePos)); //If she can see the sky continue executing
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        BlockPos pos = home.position;
        if (this.entity.getDistanceSq(home.position) > 256.0D) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vec3d((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D));
            if (vec3d != null) {
                this.entity.getNavigator().tryMoveToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0D);
            }
        } else {
            this.entity.getNavigator().tryMoveToXYZ((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 1.0D);
        }
    }

    @Override
    public void resetTask() {
        this.insidePos = home.position;
    }
}