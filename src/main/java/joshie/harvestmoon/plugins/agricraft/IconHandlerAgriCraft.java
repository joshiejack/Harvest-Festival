package joshie.harvestmoon.plugins.agricraft;

import joshie.harvestmoon.api.crops.ICropRenderHandler.PlantSection;
import joshie.harvestmoon.crops.icons.AbstractIconHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.InfinityRaider.AgriCraft.blocks.BlockModPlant;

public class IconHandlerAgriCraft extends AbstractIconHandler {
    private BlockModPlant block;
    private int maxStages;

    public IconHandlerAgriCraft(BlockModPlant block, int maxStages) {
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
