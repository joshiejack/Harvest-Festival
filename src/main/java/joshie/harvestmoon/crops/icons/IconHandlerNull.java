package joshie.harvestmoon.crops.icons;

import joshie.harvestmoon.api.crops.ICropRenderHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class IconHandlerNull implements ICropRenderHandler {
    @Override
    public IIcon getIconForStage(int stage) {
        return Blocks.air.getIcon(0, 0);
    }

    @Override
    public void registerIcons(IIconRegister register) {}
}
