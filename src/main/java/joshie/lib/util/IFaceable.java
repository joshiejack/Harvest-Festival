package joshie.lib.util;

import net.minecraftforge.common.util.ForgeDirection;

public interface IFaceable {
    void setFacing(ForgeDirection dir);
    ForgeDirection getFacing();
}
