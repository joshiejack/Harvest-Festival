package joshie.harvest.core.util.interfaces;

import net.minecraft.util.EnumFacing;

public interface IFaceable {
    void setFacing(EnumFacing dir);
    EnumFacing getFacing();
}
