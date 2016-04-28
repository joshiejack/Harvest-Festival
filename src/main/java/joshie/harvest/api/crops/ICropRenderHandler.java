package joshie.harvest.api.crops;

import net.minecraft.block.Block;

/** This is for grabbing the icons for different crops in block form **/
public interface ICropRenderHandler {
    /** Return true if this crop handles it's own rendering 
     * @param       the block renderer
     * @param       the block access
     * @param       the xcoord
     * @param       the ycoord
     * @param       the zcoord
     * @param       the block **/
    //public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block);
    
    /** Can be used to set the blocks bound, based on how
     * thie crop is rendereing
     * @param       block the block that you should perform the bounds update on
     * @param       section the section of the plant, whether it's the top or bottom half
     *              if the plant is one tall, this will pass BOTTOM
     * @param       crop the crop we are setting the bounds of
     * @param       stage the stage this crop is currently at */
    public void setBlockBoundsBasedOnStage(Block block, PlantSection section, ICrop crop, int stage);
    
    public static enum PlantSection {
        TOP, BOTTOM;
    }
}