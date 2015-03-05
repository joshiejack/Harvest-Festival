package joshie.harvestmoon.crops.icons;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class IconHandlerNull extends AbstractIconHandler {
    @Override
    public IIcon getIconForStage(PlantSection section, int stage) {
        return Blocks.air.getIcon(0, 0);
    }

    @Override
    public void registerIcons(IIconRegister register) {}
}
