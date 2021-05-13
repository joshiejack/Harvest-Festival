package uk.joshiejack.gastronomy.tile.base;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.tile.TileOven;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class TileCookingHeatable extends TileCookingTicking {
    private final SoundEvent sound;

    public TileCookingHeatable(Appliance appliance, SoundEvent sound) {
        super(appliance);
        this.sound = sound;
    }

    @Override
    public void animate() {
        TileEntity tile = world.getTileEntity(pos.down());
        if (tile instanceof TileOven) {
            if (cookTimer == 1) {
                world.playSound(getPos().getX(), getPos().getY() + 0.5D, getPos().getZ(), sound, SoundCategory.BLOCKS, 2F, world.rand.nextFloat() * 0.1F + 0.9F, false);
            }

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
    public PlaceIngredientResult hasPrereqs() {
        return isAbove(Appliance.OVEN) ? PlaceIngredientResult.SUCCESS : PlaceIngredientResult.MISSING_OVEN;
    }
}
