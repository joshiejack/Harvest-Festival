package joshie.harvestmoon.api.crops;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/** This is for grabbing the icons for different crops in block form **/
public interface ICropRenderHandler {
    /** This should return the relevant icon for this crops growth stage
     * 
     * @param       the stage this crop is at
     * @return      the icon for this stage */
    public IIcon getIconForStage(int stage);
    
    /** Register your icons
     *  @param      the icon register **/
    public void registerIcons(IIconRegister register);
}
