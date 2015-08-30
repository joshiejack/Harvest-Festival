package joshie.harvest.blocks;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockIcons {
	//Fluids
    public static IIcon OIL_STILL;
    public static IIcon OIL_FLOW;
    public static IIcon MILK_STILL;
    public static IIcon MILK_FLOW; 
    
    /** Soil Icons **/
    public static IIcon MINE_HOED;

    public static void registerBlockIcons(IIconRegister register) {
    	//Fluids
        OIL_STILL = register.registerIcon(HFModInfo.MODPATH + ":oil_still");
        OIL_FLOW = register.registerIcon(HFModInfo.MODPATH + ":oil_flow");
        MILK_STILL = register.registerIcon(HFModInfo.MODPATH + ":milk_still");
        MILK_FLOW = register.registerIcon(HFModInfo.MODPATH + ":milk_flow");
        /** Mine Hoed */
        MINE_HOED = register.registerIcon(HFModInfo.MODPATH + ":mine_hoe");
    }
}
