package joshie.harvest.core.util.generic;

import net.minecraft.util.EnumFacing;

public interface IFaceable {
    void setFacing(EnumFacing dir);
    EnumFacing getFacing();
}
