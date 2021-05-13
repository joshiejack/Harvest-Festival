package uk.joshiejack.settlements.client.animation;

import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

@PenguinLoader("sleep")
public class AnimationSleep extends Animation {
    private EnumFacing facing;

    public float getFacingInDegrees() {
        return facing == EnumFacing.SOUTH ? 90F : facing == EnumFacing.EAST? 180F: facing == EnumFacing.NORTH ? 270F: 0F;
    }

    @Override
    public boolean renderLiving(EntityNPC npc, double x, double y, double z) {
        GlStateManager.translate((float)x + (double) npc.renderOffsetX, y, (float) z + (double) npc.renderOffsetZ);
        return true;
    }

    @Override
    public boolean applyRotation(EntityNPC npc) {
        GlStateManager.rotate(getFacingInDegrees(), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(90F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
        return true;
    }

    @Override
    public Animation withData(Object... args) {
        facing = (EnumFacing) args[0];
        return this;
    }


    @Override
    public void play(EntityNPC npc) {
        npc.renderOffsetX = -1.8F * (float) facing.getXOffset();
        npc.renderOffsetZ = -1.8F * (float) facing.getZOffset();
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("Facing", (byte) facing.getIndex());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        this.facing = EnumFacing.byIndex(tag.getByte("Facing"));
    }
}
