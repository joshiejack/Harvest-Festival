package joshie.harvest.blocks;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockIcons {
    /** Generic Material Icons **/
    public static IIcon METALLIC;
    
    /** Kitchen Icons **/
    public static IIcon COUNTER_FRONT;
    public static IIcon COUNTER_SIDE;
    public static IIcon COUNTER_SURFACE;
    
    /** Soil Icons **/
    public static IIcon MINE_HOED;

    public static void registerBlockIcons(IIconRegister register) {
        /** Generic Material Icons **/
        METALLIC = register.registerIcon(HFModInfo.MODPATH + ":generic/metal");
        
        /** Kitchen Icons **/
        COUNTER_FRONT = register.registerIcon(HFModInfo.MODPATH + ":counter_front");
        COUNTER_SIDE = register.registerIcon(HFModInfo.MODPATH + ":counter_side");
        COUNTER_SURFACE = register.registerIcon(HFModInfo.MODPATH + ":counter_surface");
        
        /** Mine Hoed */
        MINE_HOED = register.registerIcon(HFModInfo.MODPATH + ":mine_hoe");
    }
}
