package joshie.lib.helpers;

import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class SideHelper {
    public static boolean isClient(World world) {
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) return true;
        else return false;
    }
}
