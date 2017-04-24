package joshie.harvest.cooking.tile;

import joshie.harvest.cooking.CookingHelper.PlaceIngredientResult;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.tile.TileCooking.TileCookingTicking;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

public abstract class TileHeatable extends TileCookingTicking {
    @Override
    public void doRenderUpdate() {
        super.doRenderUpdate();
        world.markBlockRangeForRenderUpdate(getPos().down(), getPos().down());
    }

    @Override
    public void animate() {
        TileEntity tile = world.getTileEntity(pos.down());
        if (tile instanceof TileOven) {
            EnumFacing orientation = ((TileOven)tile).getFacing();
            if (orientation == EnumFacing.NORTH || orientation == EnumFacing.SOUTH) {
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.2D + (world.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.265D + (world.rand.nextInt(15) / 100D), 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.2D + (world.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.735D - (world.rand.nextInt(15) / 100D), 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.8D - (world.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.265D + (world.rand.nextInt(15) / 100D), 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.8D - (world.rand.nextInt(10) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.735D - (world.rand.nextInt(15) / 100D), 0, 0, 0);
            } else if (orientation == EnumFacing.WEST || orientation == EnumFacing.EAST) {
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.265D + (world.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.2D + (world.rand.nextInt(10) / 100D), 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.735D - (world.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.2D + (world.rand.nextInt(10) / 100D), 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.265D + (world.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.8D - (world.rand.nextInt(10) / 100D), 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, getPos().getX() + 0.735D - (world.rand.nextInt(15) / 100D), getPos().getY() - 0.115D, getPos().getZ() + 0.8D - (world.rand.nextInt(10) / 100D), 0, 0, 0);
            }

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, getPos().getX() + 0.4D + (world.rand.nextInt(20) / 100D), getPos().getY() - 0.2D, getPos().getZ() + 0.4D + (world.rand.nextInt(20) / 100D), 0, 0, 0);
        }
    }

    @Override
    public PlaceIngredientResult hasPrerequisites() {
        return isAbove(HFCooking.OVEN) ? PlaceIngredientResult.SUCCESS : PlaceIngredientResult.MISSING_OVEN;
    }
}