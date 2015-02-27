package joshie.harvestmoon.plugins.harvestcraft;

import joshie.harvestmoon.api.crops.ICropIconHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class IconHandlerHarvestCraft implements ICropIconHandler {
    private Block block;
    private int maxStages;

    public IconHandlerHarvestCraft(Block block, int maxStages) {
        this.block = block;
        this.maxStages = maxStages;
    }

    @Override
    public IIcon getIconForStage(int stage) {
        return block.getIcon(0, (int) (((double) stage / maxStages) * 7));
    }

    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }
}
