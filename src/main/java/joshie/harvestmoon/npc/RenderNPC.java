package joshie.harvestmoon.npc;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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

        float f2, f4;

        ItemStack stack = npc.inventory.getCurrentItem();

        if (stack != null) {
            GL11.glPushMatrix();
            modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            EnumAction enumaction = null;

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(stack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, stack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (is3D || stack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack.getItem()).getRenderType())) {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f2 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f2, -f2, f2);
            } else if (stack.getItem() == Items.bow) {
                f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else if (stack.getItem().isFull3D()) {
                f2 = 0.625F;

                if (stack.getItem().shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                f2 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f2, f2, f2);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            float f3;
            int k;
            float f12;

            if (stack.getItem().requiresMultipleRenderPasses()) {
                for (k = 0; k < stack.getItem().getRenderPasses(stack.getItemDamage()); ++k) {
                    int i = stack.getItem().getColorFromItemStack(stack, k);
                    f12 = (float) (i >> 16 & 255) / 255.0F;
                    f3 = (float) (i >> 8 & 255) / 255.0F;
                    f4 = (float) (i & 255) / 255.0F;
                    GL11.glColor4f(f12, f3, f4, 1.0F);
                    renderManager.itemRenderer.renderItem(npc, stack, k);
                }
            } else {
                k = stack.getItem().getColorFromItemStack(stack, 0);
                float f11 = (float) (k >> 16 & 255) / 255.0F;
                f12 = (float) (k >> 8 & 255) / 255.0F;
                f3 = (float) (k & 255) / 255.0F;
                GL11.glColor4f(f11, f12, f3, 1.0F);
                renderManager.itemRenderer.renderItem(npc, stack, 0);
            }

            GL11.glPopMatrix();
        }
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
        if (npc.getNPC().isChild()) {
            modelBipedMain.isChild = true;
            mainModel.isChild = true;
            if (renderPassModel != null) {
                renderPassModel.isChild = true;
            }
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        ItemStack stack = npc.inventory.getCurrentItem();
        modelBipedMain.heldItemRight = stack != null ? 1 : 0;

        modelBipedMain.isSneak = npc.isSneaking();
        double d3 = y - (double) npc.yOffset;
        super.doRender((EntityLivingBase) npc, x, d3, z, pitch, yaw);
        modelBipedMain.aimedBow = false;
        modelBipedMain.isSneak = false;
        modelBipedMain.heldItemRight = 0;
    }
}