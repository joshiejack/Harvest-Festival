package joshie.harvest.npc.entity;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NPCHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityAIGoHome extends EntityAIBase {
    private WorldLocation home;
    private EntityNPC entity;
    private int insidePosX = -1;
    private int insidePosZ = -1;
    private int insidePosY = -1;

    public EntityAIGoHome(EntityNPC npc) {
        entity = npc;
        home = NPCHelper.getHomeForEntity(entity);
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (home == null) return false;
        int i = MathHelper.floor_double(this.entity.posX);
        int j = MathHelper.floor_double(this.entity.posY);
        int k = MathHelper.floor_double(this.entity.posZ);
        int bedTime = CalendarHelper.getScaledTime(entity.getNPC().getBedtime() - 1500);
        long time = CalendarHelper.getTime(entity.worldObj);
        if (time >= bedTime) {
            return (entity.worldObj.canBlockSeeTheSky(i, j, k));
        }

        if ((!entity.worldObj.isDaytime() || entity.worldObj.isRaining() || !this.entity.worldObj.getBiomeGenForCoords(i, k).canSpawnLightningBolt()) && !this.entity.worldObj.provider.hasNoSky) { //If it's raining or night, send the npc home
            //If the entity is not inside
            return (entity.worldObj.canBlockSeeTheSky(i, j, k)); //If she can see the sky continue executing
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.insidePosX = -1;
        if (this.entity.getDistanceSq((double) home.x, (double) home.y, (double) home.z) > 256.0D) {
            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, Vec3.createVectorHelper((double) home.x + 0.5D, (double) home.y, (double) home.z + 0.5D));
            if (vec3 != null) {
                this.entity.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
            }
        } else {
            this.entity.getNavigator().tryMoveToXYZ((double) home.x + 0.5D, (double) home.y, (double) home.z + 0.5D, 1.0D);
        }
    }

    @Override
    public void resetTask() {
        this.insidePosX = home.x;
        this.insidePosZ = home.z;
        this.insidePosY = home.y;
    }
}