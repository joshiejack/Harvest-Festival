package joshie.harvest.plugins.harvestcraft;

import joshie.harvest.crops.icons.AbstractIconHandler;
import net.minecraft.block.Block;

public class IconHandlerHarvestCraft extends AbstractIconHandler {
    private Block block;
    private int maxStages;

    public IconHandlerHarvestCraft(Block block, int maxStages) {
        this.block = block;
        this.maxStages = maxStages;
    }

    @Override
    public IIcon getIconForStage(PlantSection section, int stage) {
        return block.getIcon(0, (int) (((double) stage / maxStages) * 7));
    }

    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }
}
