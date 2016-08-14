package joshie.harvest.core.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import joshie.harvest.core.render.FakeEntityRenderer.FakeEntityTile;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class FakeEntityRenderer extends TileEntitySpecialRenderer<FakeEntityTile> {
    private ModelBase model;
    private ResourceLocation resource;

    public FakeEntityRenderer(String name, ModelBase model) {
        this.model = model;
        this.model.isChild = false;
        this.resource = new ResourceLocation(MODID, "textures/entity/" + name + ".png");
    }

    @Override
    public void renderTileEntityAt(FakeEntityTile fake, double x, double y, double z, float partialTicks, int destroyStage) {
         GlStateManager.pushMatrix();
         GlStateManager.translate(0.5F, -0.05F, 0.5F);
         GlStateManager.scale(-0.75F, 0.75F, 0.75F);
         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         GlStateManager.enableRescaleNormal();
         GlStateManager.scale(-1.0F, -1.0F, 1.0F);
         GlStateManager.translate(0.0F, -1.501F, 0.0F);
         bindTexture(resource);
         model.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
         model.isChild = false;
         GlStateManager.disableRescaleNormal();
         GlStateManager.enableCull();
         GlStateManager.popMatrix();
         GlStateManager.popMatrix();
    }

    public static class FakeEntityTile extends TileEntity {}
}
