package joshie.harvestmoon.blocks.render;

import joshie.harvestmoon.lib.HMModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExtraIcons {
    /** Generic Material Icons **/
    public static IIcon METALLIC;
    
    /** Kitchen Icons **/
    public static IIcon COUNTER_FRONT;
    public static IIcon COUNTER_SIDE;
    public static IIcon COUNTER_SURFACE;
    public static IIcon FRIDGE_BACK;
    public static IIcon FRIDGE_SIDE;
    public static IIcon FRIDGE_TOP;
    public static IIcon FRIDGE_BOTTOM;

    public static void registerBlockIcons(IIconRegister register) {
        /** Generic Material Icons **/
        METALLIC = register.registerIcon(HMModInfo.MODPATH + ":generic/metal");
        
        /** Kitchen Icons **/
        COUNTER_FRONT = register.registerIcon(HMModInfo.MODPATH + ":counter_front");
        COUNTER_SIDE = register.registerIcon(HMModInfo.MODPATH + ":counter_side");
        COUNTER_SURFACE = register.registerIcon(HMModInfo.MODPATH + ":counter_surface");
        FRIDGE_BACK = register.registerIcon(HMModInfo.MODPATH + ":fridge_back");
        FRIDGE_SIDE = register.registerIcon(HMModInfo.MODPATH + ":fridge_sides");
        FRIDGE_TOP = register.registerIcon(HMModInfo.MODPATH + ":fridge_upper_door");
        FRIDGE_BOTTOM = register.registerIcon(HMModInfo.MODPATH + ":fridge_lower_door");
    }
}
