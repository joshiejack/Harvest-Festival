package joshie.harvestmoon.api.crops;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/** This is for grabbing the icons for different crops in block form **/
public interface ICropRenderHandler {
    /** This should return the relevant icon for this crops growth stage
     * 
     * @param       the section of the plant, whether it's the top or bottom half
     *              (section is only valid when it's a double plant)
     * @param       the stage this crop is at
     * @return      the icon for this stage */
    public IIcon getIconForStage(PlantSection section, int stage);
    
    /** Register your icons
     *  @param      the icon register **/
    public void registerIcons(IIconRegister register);
    
    /** Return true if this crop handles it's own rendering 
     * @param  the block renderer
     * @param       the block access
     * @param       the xcoord
     * @param       the ycoord
     * @param       the zcoord
     * @param       the block **/
    public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block);
    
    public static enum PlantSection {
        TOP, BOTTOM;
    }
}
