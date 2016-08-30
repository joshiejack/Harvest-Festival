package joshie.harvest.cooking.render;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.cooking.tile.TileFridge;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.util.EnumFacing.*;

@HFQuest
@SideOnly(Side.CLIENT)
public class SpecialRendererFridge extends TileEntitySpecialRenderer<TileFridge> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/models/fridge_door.png");
    private final ModelFridgeDoor ovenModel = new ModelFridgeDoor();

    @Override
    public final void renderTileEntityAt(@Nonnull TileFridge fridge, double x, double y, double z, float partialTicks, int destroyStage) {
        if (destroyStage >= 0) {
            bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            bindTexture(TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        if (destroyStage < 0) GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        EnumFacing facing = fridge.getFacing();
        int j = facing == NORTH ? 180 : facing == SOUTH ? 0 : facing == WEST ? 90 : -90;
        if (facing == SOUTH) GlStateManager.translate(1F, 0F, 1F);
        else if (facing == WEST) GlStateManager.translate(1F, 0F, 0F);
        else if (facing == EAST) GlStateManager.translate(0F, 0F, 1F);

        GlStateManager.rotate((float) j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        //Top Door
        float f = fridge.prevLidAngleTop + (fridge.lidAngleTop - fridge.prevLidAngleTop) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        ovenModel.topDoor.rotateAngleY = -(f * ((float) Math.PI / 2F));

        //Bottom Door
        f = fridge.prevLidAngleBottom + (fridge.lidAngleBottom - fridge.prevLidAngleBottom) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        ovenModel.bottomDoor.rotateAngleY = -(f * ((float) Math.PI / 2F));

        //Render
        ovenModel.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}