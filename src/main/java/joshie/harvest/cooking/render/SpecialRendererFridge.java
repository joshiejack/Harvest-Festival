package joshie.harvest.cooking.render;

import joshie.harvest.api.HFQuest;
import joshie.harvest.cooking.blocks.TileFridge;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@HFQuest
public class SpecialRendererFridge extends TileEntitySpecialRenderer<TileFridge> {
    @Override
    public final void renderTileEntityAt(TileFridge tile, double x, double y, double z, float tick, int destroyStage) {

    }
}