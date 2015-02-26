package joshie.harvestmoon.crops;

import joshie.harvestmoon.calendar.Season;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CropWheat extends Crop {
    public Block block;

    /** Defaults to 8 stages of growth, with no regrowing 
     * @param color */
    public CropWheat(String unlocalized, Season[] season, int cost, int sell, int year, Block block, int color) {
        super(unlocalized, season, cost, sell, 28, 0, year, color);
        this.block = block;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int stage) {
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
    public void registerIcons(IIconRegister register) {
        return;
    }
}
