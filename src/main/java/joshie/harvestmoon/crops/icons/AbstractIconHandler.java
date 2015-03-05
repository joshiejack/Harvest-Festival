package joshie.harvestmoon.crops.icons;

import joshie.harvestmoon.api.crops.ICropRenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public abstract class AbstractIconHandler implements ICropRenderHandler {
    protected IIcon[] stageIcons;
    
    @Override
    public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block) {
        return false;
    }
}
