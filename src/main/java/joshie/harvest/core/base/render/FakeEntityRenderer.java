package joshie.harvest.core.base.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.animals.render.ModelHarvestChicken;
import joshie.harvest.animals.render.ModelHarvestCow;
import joshie.harvest.animals.render.ModelHarvestSheep;
import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.core.helpers.StackRenderHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class FakeEntityRenderer extends TileEntitySpecialRenderer<EntityItemRenderer> {
    public static final ResourceLocation SHADOW = new ResourceLocation(MODID, "textures/entity/shadow.png");
    public static final FakeEntityRenderer INSTANCE = new FakeEntityRenderer();

    @Override
    public void render(@Nullable EntityItemRenderer fake, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
         if (fake != null) {
             GlStateManager.pushMatrix();
             GlStateManager.translate(fake.render.translation, -0.05F, 0.5F);
             GlStateManager.scale(-fake.render.scale, fake.render.scale, fake.render.scale);
             GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
             GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
             GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
             GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
             GlStateManager.pushMatrix();
             GlStateManager.disableCull();
             GlStateManager.enableRescaleNormal();
             GlStateManager.scale(-1.0F, -1.0F, 1.0F);
             GlStateManager.translate(0.0F, -1.501F, 0.0F);
             if (StackRenderHelper.renderShadow) {
                 bindTexture(SHADOW);
                 GlStateManager.disableLighting();
             } else bindTexture(fake.render.texture);

             fake.render.model.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
             if (StackRenderHelper.renderShadow) GlStateManager.enableLighting();
             GlStateManager.disableRescaleNormal();
             GlStateManager.enableCull();
             GlStateManager.popMatrix();
             GlStateManager.popMatrix();
         }
    }

    public abstract static class EntityItemRenderer extends TileEntity {
        protected final TIntObjectMap<RenderPair> map = new TIntObjectHashMap<>();
        public RenderPair render;

        public void setStack(@Nonnull ItemStack stack) {
            this.render = map.get(stack.getItemDamage());
        }
    }

    public static class RenderPair {
        public final ModelBase model;
        public final ResourceLocation texture;
        public final float scale;
        public final float translation;

        public RenderPair(ResourceLocation name, ModelBase model) {
            this.model = model;
            this.model.isChild = false;
            this.texture = name;
            //Vanilla
            this.translation = 0.5F;
            this.scale = 0.75F;
        }

        public RenderPair(String name, ModelBase model) {
            this.model = model;
            this.model.isChild = false;
            this.texture = new ResourceLocation(MODID, "textures/entity/" + name + ".png");
            //Modded
            if (model instanceof ModelHarvestChicken.Child) {
                this.translation = 0.5F;
                this.scale = 2F;
            } else if (model instanceof ModelHarvestChicken.Adult) {
                this.translation = 0.5F;
                this.scale = 1.5F;
            } else if (model instanceof ModelHarvestSheep.Wooly) {
                this.translation = 0.6F;
                this.scale = 0.9F;
            } else if (model instanceof ModelHarvestCow.Adult) {
                this.translation = 0.525F;
                this.scale = 0.7F;
            } else {
                this.translation = 0.5F;
                this.scale = 0.75F;
            }
        }
    }
}
