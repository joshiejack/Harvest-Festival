package joshie.harvestmoon.crops.render;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.lib.HMModInfo.CROPPATH;
import joshie.harvestmoon.util.RenderBase;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class RenderCrop extends RenderBase {
    private static IIcon[] colors;
    private boolean isGiant;
    private int stage;

    public boolean render(RenderBlocks render, IBlockAccess world, int x, int y, int z) {
        this.isGiant = handler.getClient().getCropTracker().isCropGiant(joshie.lib.helpers.ClientHelper.getWorld(), x, y, z);
        this.stage = handler.getClient().getCropTracker().getCropStage(joshie.lib.helpers.ClientHelper.getWorld(), x, y, z);
        return super.render(render, world, x, y, z);
    }

    public abstract void renderCrop(int stage);

    @Override
    public void renderBlock() {
        if (!isItem()) {
            renderCrop(stage);
        }
    }

    @Override
    protected void renderBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super.renderBlock(minX, minY - 0.063D, minZ, maxX, maxY - 0.063D, maxZ);
    }

    public IIcon getColor(Colors c) {
        return colors[c.ordinal()];
    }

    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister register) {
        colors = new IIcon[Colors.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = register.registerIcon(CROPPATH + "color_" + Colors.values()[i].name().toLowerCase());
        }
    }

    protected enum Colors {
        STRAWBERRY;
    }
}
