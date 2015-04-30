package joshie.harvestmoon.blocks;

import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockIcons {
    /** Generic Material Icons **/
    public static IIcon METALLIC;
    public static IIcon GLASS;
    public static IIcon BLACK_PLASTIC;
    public static IIcon WHITE_PLASTIC;
    public static IIcon RED_PLASTIC;
    public static IIcon BLUE_PLASTIC;
    public static IIcon MATTE;
    public static IIcon RAINBOW;
    
    /** Kitchen Icons **/
    public static IIcon COUNTER_FRONT;
    public static IIcon COUNTER_SIDE;
    public static IIcon COUNTER_SURFACE;
    public static IIcon FRIDGE_BACK;
    public static IIcon FRIDGE_SIDE;
    public static IIcon FRIDGE_TOP;
    public static IIcon FRIDGE_BOTTOM;
    public static IIcon PAN_BOTTOM;
    public static IIcon PAN_SIDE;
    
    /** Soil Icons **/
    public static IIcon MINE_HOED;

    public static void registerBlockIcons(IIconRegister register) {
        /** Generic Material Icons **/
        METALLIC = register.registerIcon(HMModInfo.MODPATH + ":generic/metal");
        GLASS = register.registerIcon(HMModInfo.MODPATH + ":generic/glass");
        BLACK_PLASTIC = register.registerIcon(HMModInfo.MODPATH + ":generic/plastic_black");
        WHITE_PLASTIC = register.registerIcon(HMModInfo.MODPATH + ":generic/plastic_white");
        RED_PLASTIC = register.registerIcon(HMModInfo.MODPATH + ":generic/plastic_red");
        BLUE_PLASTIC = register.registerIcon(HMModInfo.MODPATH + ":generic/plastic_blue");
        MATTE = register.registerIcon(HMModInfo.MODPATH + ":generic/matte");
        RAINBOW = register.registerIcon(HMModInfo.MODPATH + ":generic/rainbow");
        
        /** Kitchen Icons **/
        COUNTER_FRONT = register.registerIcon(HMModInfo.MODPATH + ":counter_front");
        COUNTER_SIDE = register.registerIcon(HMModInfo.MODPATH + ":counter_side");
        COUNTER_SURFACE = register.registerIcon(HMModInfo.MODPATH + ":counter_surface");
        FRIDGE_BACK = register.registerIcon(HMModInfo.MODPATH + ":fridge_back");
        FRIDGE_SIDE = register.registerIcon(HMModInfo.MODPATH + ":fridge_sides");
        FRIDGE_TOP = register.registerIcon(HMModInfo.MODPATH + ":fridge_upper_door");
        FRIDGE_BOTTOM = register.registerIcon(HMModInfo.MODPATH + ":fridge_lower_door");
        PAN_BOTTOM = register.registerIcon(HMModInfo.MODPATH + ":pan_base");
        PAN_SIDE = register.registerIcon(HMModInfo.MODPATH + ":pan_side");
        
        /** Mine Hoed */
        MINE_HOED = register.registerIcon(HMModInfo.MODPATH + ":mine_hoe");
    }
}
