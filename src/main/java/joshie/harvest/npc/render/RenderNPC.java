package joshie.harvest.npc.render;

import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNPC extends RenderLiving<EntityNPC> {
    private static final ModelNPC STEVE = new ModelNPC(false);
    private static final ModelNPC ALEX = new ModelNPC(true);

    public RenderNPC(RenderManager renderManager) {
        super(renderManager, STEVE, 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC npc) {
        return npc.getSkin();
    }

    @Override
    public ModelNPC  getMainModel() {
        return (ModelNPC ) super.getMainModel();
    }

    @Override
    protected void preRenderCallback(EntityNPC npc, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    protected boolean canRenderName(EntityNPC npc) {
        if (npc.hideName) return false;
        else return super.canRenderName(npc);
    }

    //Renders the Entity
    @Override
    public void doRender(EntityNPC npc, double x, double y, double z, float entityYaw, float partialTicks) {
        updateModel(npc);
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

        if (npc.isSneaking()) {
            y -= npc.getYOffset();
        }

        super.doRender(npc, x, y, z, entityYaw, partialTicks);
        GlStateManager.popMatrix();
    }

    private void updateModel(EntityNPC npc) {
        mainModel = npc.getNPC().isAlexSkin() ? ALEX : STEVE;
    }
}