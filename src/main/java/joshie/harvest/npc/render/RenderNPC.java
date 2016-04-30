package joshie.harvest.npc.render;

import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPC.Mode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNPC extends RenderLivingBase<EntityNPC> {

    public RenderNPC(RenderManager renderManager) {
        super(renderManager, new ModelNPC(), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    @Override
    public ModelNPC getMainModel() {
        return (ModelNPC) super.getMainModel();
    }

    @Override
    protected void preRenderCallback(EntityNPC npc, float partialTickTime) {
        float f1 = 0.9375F;
        GlStateManager.scale(f1, f1, f1);
    }

    @Override
    protected int getColorMultiplier(EntityNPC npc, float lightBrightness, float partialTickTime) {
        return -1;
    }

    @Override
    protected boolean canRenderName(EntityNPC npc) {
        if (npc.hideName) return false;
        else return super.canRenderName(npc);
    }

    //Renders the Entity
    @Override
    public void doRender(EntityNPC npc, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        if (npc.getNPC().isChild()) {
            getMainModel().isChild = true;
            mainModel.isChild = true;
            if (getMainModel() != null) {
                getMainModel().isChild = true;
            }
        }

        GlStateManager.scale(npc.getNPC().getHeight(), npc.getNPC().getHeight(), npc.getNPC().getHeight());
        GlStateManager.translate(0F, npc.getNPC().getOffset(), 0F);

        double d0 = y;

        if (npc.isSneaking()) {
            d0 = y - npc.getYOffset();
        }

        if (npc.getMode() == Mode.GIFT) {
            //npc.limbSwing = 45F;
        }

        super.doRender(npc, x, d0, z, entityYaw, partialTicks);

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC npc) {
        return npc.getSkin();
    }
}