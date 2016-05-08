package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

public class TileHeatable extends TileCooking {
    @Override
    public void animate(IUtensil utensil) {
        EnumFacing orientation = ((TileOven)worldObj.getTileEntity(pos.down())).orientation;
        if (orientation == EnumFacing.NORTH) {
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.2D + (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  + 0.265D + (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.2D + (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  + 0.735D - (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.8D - (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  + 0.265D + (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.8D - (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  + 0.735D - (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, getPos().getX() + 0.4D + (worldObj.rand.nextInt(20) / 100D), getPos().getY() - 0.2D, getPos().getZ() +  + 0.4D + (worldObj.rand.nextInt(20) / 100D), 0, 0, 0);
        } else if (orientation == EnumFacing.EAST) {

        } else if (orientation == EnumFacing.SOUTH) {
        } else if (orientation == EnumFacing.WEST) {

        }
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.OVEN);
    }
}