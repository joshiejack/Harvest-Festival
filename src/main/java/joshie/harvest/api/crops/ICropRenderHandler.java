package joshie.harvest.api.crops;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

/** This is for grabbing the icons for different crops in block form **/
public interface ICropRenderHandler {
    /** This should return the relevant icon for this crops growth stage
     * 
     * @param       the section of the plant, whether it's the top or bottom half
     *              (section is only valid when it's a double plant)
     *              if the plant is one tall, this will pass BOTTOM
     * @param       the stage this crop is at
     * @return      the icon for this stage */
    public IIcon getIconForStage(PlantSection section, int stage);
    
    /** Register your icons
     *  @param      the icon register **/
    public void registerIcons(IIconRegister register);
    
    /** Return true if this crop handles it's own rendering 
     * @param       the block renderer
     * @param       the block access
     * @param       the xcoord
     * @param       the ycoord
     * @param       the zcoord
     * @param       the block **/
    public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block);
    
    /** Can be used to set the blocks bound, based on how
     * thie crop is rendereing
     * @param       the block that you should perform the bounds update on
     * @param       the section of the plant, whether it's the top or bottom half
     *              if the plant is one tall, this will pass BOTTOM
     * @param       the crop we are setting the bounds of
     * @param       the stage this crop is currently at */
    public void setBlockBoundsBasedOnStage(Block block, PlantSection section, ICrop crop, int stage);
    
    public static enum PlantSection {
        TOP, BOTTOM;
    }
}
