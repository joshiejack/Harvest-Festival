package uk.joshiejack.penguinlib.util.interfaces;

import net.minecraft.util.EnumFacing;

public interface Rotatable {
    void setFacing(EnumFacing facingFromEntity);
    EnumFacing getFacing();
}
