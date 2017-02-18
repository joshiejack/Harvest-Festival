package joshie.harvest.core.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.core.base.tile.TileStand;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SpecialRendererStand<T extends TileStand> extends TileSpecialRendererItem<T> {
    private float getRotationFromFacing(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 90F;
            case EAST:
                return 0F;
            case SOUTH:
                return 270F;
            case WEST:
                return 180F;
            default:
                return 0F;
        }
    }

    @Override
    public void renderTileEntityAt(@Nonnull T tile, double x, double y, double z, float tick, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (tile.getContents() != null) {
            renderItem(tile.getContents(), 0F, getRotationFromFacing(tile.getFacing()), 0F, 0F);
        }

        GlStateManager.popMatrix();
    }


}
