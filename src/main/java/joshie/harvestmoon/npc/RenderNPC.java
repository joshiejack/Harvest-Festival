package joshie.harvestmoon.npc;

import joshie.harvestmoon.init.HMNPCs;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNPC extends RendererLivingEntity {
    private static final ResourceLocation steveTextures = new ResourceLocation("textures/entity/steve.png");
    public ModelBiped modelBipedMain;

    public RenderNPC() {
        super(new ModelBiped(0.0F), 0.5F);
        this.modelBipedMain = (ModelBiped) this.mainModel;
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase living, float pitch) {
        renderEquippedItems((EntityNPC) living, pitch);
    }

    private void renderEquippedItems(EntityNPC npc, float pitch) {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        super.renderEquippedItems(npc, pitch);
        super.renderArrowsStuckInEntity(npc, pitch);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase living, float pitch) {
        float f1 = 0.9375F;
        GL11.glScalef(f1, f1, f1);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase living, int p_77032_2_, float p_77032_3_) {
        return -1;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ((EntityNPC) entity).getSkin();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float pitch, float yaw) {
        doRender((EntityNPC) entity, x, y, z, pitch, yaw);
    }

    @Override
    public void doRender(EntityLivingBase living, double x, double y, double z, float pitch, float yaw) {
        doRender((EntityNPC) living, x, y, z, pitch, yaw);
    }

    //Renders the Entity
    private void doRender(EntityNPC npc, double x, double y, double z, float pitch, float yaw) {
        GL11.glPushMatrix();
        if (npc.getNPC().isChild()) {
            modelBipedMain.isChild = true;
            mainModel.isChild = true;
            if (renderPassModel != null) {
                renderPassModel.isChild = true;
            }
        }

        GL11.glScalef(1F, npc.getNPC().getHeight(), 1F);
        GL11.glTranslatef(0F, npc.getNPC().getOffset(), 0F);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        modelBipedMain.heldItemRight = 0;
        modelBipedMain.isSneak = npc.isSneaking();
        double d3 = y - (double) npc.yOffset;
        super.doRender((EntityLivingBase) npc, x, d3, z, pitch, yaw);
        modelBipedMain.aimedBow = false;
        modelBipedMain.isSneak = false;
        modelBipedMain.heldItemRight = 0;
        GL11.glPopMatrix();
    }
}