package joshie.harvest.crops.icons;

import net.minecraft.init.Blocks;

public class IconHandlerNull extends AbstractIconHandler {
    @Override
    public IIcon getIconForStage(PlantSection section, int stage) {
        return Blocks.air.getIcon(0, 0);
    }

    @Override
    public void registerIcons(IIconRegister register) {}
}
