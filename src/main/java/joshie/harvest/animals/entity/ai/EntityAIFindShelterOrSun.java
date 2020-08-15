package joshie.harvest.animals.entity.ai;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityAIFindShelterOrSun extends EntityAIBase {
    private final EntityAnimal theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World world;

    public EntityAIFindShelterOrSun(EntityAnimal theCreatureIn) {
        this.theCreature = theCreatureIn;
        this.movementSpeed = 1D;
        this.world = theCreatureIn.world;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        boolean outside = world.canSeeSky(new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ));
        CalendarDate date = HFApi.calendar.getDate(world);
        if (outside && (!world.isDaytime() || world.isRaining() || date.getSeason() == Season.WINTER)) {
            Vec3d vec3d = findLocation(false);
            if (vec3d == null) {
                return false;
            } else {
                shelterX = vec3d.xCoord;
                shelterY = vec3d.yCoord;
                shelterZ = vec3d.zCoord;
                return true;
            }
        }
        else if (!outside && (world.isDaytime() && !world.isRaining() && date.getSeason() != Season.WINTER)) {
            Vec3d vec3d = findLocation(true);
            if (vec3d == null) {
                return false;
            } else {
                shelterX = vec3d.xCoord;
                shelterY = vec3d.yCoord;
                shelterZ = vec3d.zCoord;
                return true;
            }
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return !theCreature.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        theCreature.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, movementSpeed);
    }

    @Nullable
    private Vec3d findLocation(boolean outside)  {
        Random random = theCreature.getRNG();
        BlockPos blockpos = new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ);

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (world.canSeeSky(blockpos1) == outside && theCreature.getBlockPathWeight(blockpos1) < 0.0F) {
                return new Vec3d((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
            }
        }

        return null;
    }
}