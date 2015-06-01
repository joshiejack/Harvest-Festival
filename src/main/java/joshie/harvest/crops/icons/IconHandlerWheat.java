package joshie.harvest.crops.icons;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerWheat extends AbstractIconHandler {
    private Block block;

    public IconHandlerWheat() {
        block = Blocks.wheat;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconForStage(PlantSection section, int stage) {
        if (stage <= 2) return block.getIcon(0, 0);
        if (stage <= 5) return block.getIcon(0, 1);
        if (stage <= 9) return block.getIcon(0, 2);
        if (stage <= 12) return block.getIcon(0, 3);
        if (stage <= 15) return block.getIcon(0, 4);
        if (stage <= 20) return block.getIcon(0, 5);
        if (stage <= 27) return block.getIcon(0, 6);
        return stage == 28 ? block.getIcon(0, 7) : block.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }
}
