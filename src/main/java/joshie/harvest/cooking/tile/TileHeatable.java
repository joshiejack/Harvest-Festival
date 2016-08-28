package joshie.harvest.cooking.tile;

import joshie.harvest.cooking.tile.TileCooking.TileCookingTicking;
import joshie.harvest.api.cooking.Utensil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

public abstract class TileHeatable extends TileCookingTicking {
    @Override
    public void doRenderUpdate() {
        super.doRenderUpdate();
        worldObj.markBlockRangeForRenderUpdate(getPos().down(), getPos().down());
    }

    @Override
    public void animate() {
        EnumFacing orientation = ((TileOven)worldObj.getTileEntity(pos.down())).orientation;
        if (orientation == EnumFacing.NORTH || orientation == EnumFacing.SOUTH) {
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.2D + (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.265D + (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.2D + (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.735D - (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.8D - (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.265D + (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.8D - (worldObj.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.735D - (worldObj.rand.nextInt(15) / 100D), 0, 0, 0);
        } else if (orientation == EnumFacing.WEST || orientation == EnumFacing.EAST) {
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.265D + (worldObj.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  0.2D + (worldObj.rand.nextInt(10) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.735D - (worldObj.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  0.2D + (worldObj.rand.nextInt(10) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.265D + (worldObj.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  0.8D - (worldObj.rand.nextInt(10) / 100D), 0, 0, 0);
            worldObj.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.735D - (worldObj.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() +  0.8D - (worldObj.rand.nextInt(10) / 100D), 0, 0, 0);
        }

        worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, getPos().getX() + 0.4D + (worldObj.rand.nextInt(20) / 100D), getPos().getY() - 0.2D, getPos().getZ() + 0.4D + (worldObj.rand.nextInt(20) / 100D), 0, 0, 0);
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.OVEN);
    }
}