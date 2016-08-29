package joshie.harvest.core.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.render.FakeEntityRenderer.EntityItemRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class FakeEntityRenderer extends TileEntitySpecialRenderer<EntityItemRenderer> {
    public static final FakeEntityRenderer INSTANCE = new FakeEntityRenderer();

    @Override
    public void renderTileEntityAt(@Nullable EntityItemRenderer fake, double x, double y, double z, float partialTicks, int destroyStage) {
         if (fake != null) {
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
             bindTexture(fake.render.texture);
             fake.render.model.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
             GlStateManager.disableRescaleNormal();
             GlStateManager.enableCull();
             GlStateManager.popMatrix();
             GlStateManager.popMatrix();
         }
    }

    public abstract static class EntityItemRenderer extends TileEntity {
        protected final TIntObjectMap<RenderPair> map = new TIntObjectHashMap<>();
        public RenderPair render;

        public void setID(int id) {
            this.render = map.get(id);
        }
    }

    public static class RenderPair {
        public ModelBase model;
        public ResourceLocation texture;

        public RenderPair(String name, ModelBase model) {
            this.model = model;
            this.model.isChild = false;
            this.texture = new ResourceLocation(MODID, "textures/entity/" + name + ".png");
        }
    }
}
