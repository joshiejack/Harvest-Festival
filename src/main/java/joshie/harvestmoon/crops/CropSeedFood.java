package joshie.harvestmoon.crops;

import joshie.harvestmoon.calendar.Season;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CropSeedFood extends Crop {
    public Block block;

    /** Defaults to 8 stages of growth, with no regrowing 
     * @param color */
    public CropSeedFood(String unlocalized, Season season, int cost, int sell, int year, Block block, int color) {
        super(unlocalized, season, cost, sell, 8, 0, year, color);
        this.block = block;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int stage) {
        if (stage == 1) return block.getIcon(0, 0);
        if (stage == 2 || stage == 3 || stage == 4) return block.getIcon(0, 2);
        if (stage == 5 || stage == 6 || stage == 7) return block.getIcon(0, 4);
        return stage == 8 ? block.getIcon(0, 7) : block.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        return;
    }
}
