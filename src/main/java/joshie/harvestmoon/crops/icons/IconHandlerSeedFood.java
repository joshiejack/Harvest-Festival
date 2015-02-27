package joshie.harvestmoon.crops.icons;

import joshie.harvestmoon.api.crops.ICropIconHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerSeedFood implements ICropIconHandler {
    private Block block;

    public IconHandlerSeedFood(Block block) {
        this.block = block;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconForStage(int stage) {
        if (stage == 1) return block.getIcon(0, 0);
        if (stage == 2 || stage == 3 || stage == 4) return block.getIcon(0, 2);
        if (stage == 5 || stage == 6 || stage == 7) return block.getIcon(0, 4);
        return stage == 8 ? block.getIcon(0, 7) : block.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        return;
    }
}
