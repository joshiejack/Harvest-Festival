package joshie.harvest.npc;

import java.util.UUID;

import joshie.harvest.npc.EntityNPC.Mode;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNPC extends RendererLivingEntity {
    private static final ResourceLocation steveTextures = new ResourceLocation("textures/entity/steve.png");
    public ModelNPC modelBipedMain;

    public RenderNPC() {
        super(new ModelNPC(), 0.5F);
        this.modelBipedMain = (ModelNPC) this.mainModel;
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase living, float pitch) {
        renderEquippedItems((EntityNPC) living, pitch);
    }

    private void renderEquippedItems(EntityNPC npc, float pitch) {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        super.renderEquippedItems(npc, pitch);
        super.renderArrowsStuckInEntity(npc, pitch);
        ItemStack itemstack = npc.getEquipmentInSlot(3);
        if (itemstack != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            float f1;

            if (itemstack.getItem() instanceof ItemBlock) {
                net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
                boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
                    f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }

                this.renderManager.itemRenderer.renderItem(npc, itemstack, 0);
            } else if (itemstack.getItem() == Items.skull) {
                f1 = 1.0625F;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile gameprofile = null;

                if (itemstack.hasTagCompound()) {
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
                        gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
                    }
                }

                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), gameprofile);
            }

            GL11.glPopMatrix();
        }

        float f2;

        boolean flag = false;
        float f4;

        ItemStack itemstack1 = npc.getEquipmentInSlot(0);
        if (itemstack1 != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            EnumAction enumaction = null;
            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack1, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (is3D || itemstack1.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack1.getItem()).getRenderType())) {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f2 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f2, -f2, f2);
            } else if (itemstack1.getItem() == Items.bow) {
                f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else if (itemstack1.getItem().isFull3D()) {
                f2 = 0.625F;

                if (itemstack1.getItem().shouldRotateAroundWhenRendering()) {
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

            if (itemstack1.getItem().requiresMultipleRenderPasses()) {
                for (k = 0; k < itemstack1.getItem().getRenderPasses(itemstack1.getItemDamage()); ++k) {
                    int i = itemstack1.getItem().getColorFromItemStack(itemstack1, k);
                    f12 = (float) (i >> 16 & 255) / 255.0F;
                    f3 = (float) (i >> 8 & 255) / 255.0F;
                    f4 = (float) (i & 255) / 255.0F;
                    GL11.glColor4f(f12, f3, f4, 1.0F);
                    this.renderManager.itemRenderer.renderItem(npc, itemstack1, k);
                }
            } else {
                k = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
                float f11 = (float) (k >> 16 & 255) / 255.0F;
                f12 = (float) (k >> 8 & 255) / 255.0F;
                f3 = (float) (k & 255) / 255.0F;
                GL11.glColor4f(f11, f12, f3, 1.0F);
                this.renderManager.itemRenderer.renderItem(npc, itemstack1, 0);
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
    protected boolean func_110813_b(EntityLivingBase entity) {
        if (((EntityNPC) entity).hideName) return false;
        else return super.func_110813_b(entity);
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

        GL11.glScalef(npc.getNPC().getHeight(), npc.getNPC().getHeight(), npc.getNPC().getHeight());
        GL11.glTranslatef(0F, npc.getNPC().getOffset(), 0F);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        //modelBipedMain.heldItemRight = 0;
        modelBipedMain.isSneak = npc.isSneaking();
        double d3 = y - (double) npc.yOffset;

        if (npc.getMode() == Mode.GIFT) {
            //npc.limbSwing = 45F;
        }

        super.doRender((EntityLivingBase) npc, x, d3, z, pitch, yaw);
        //modelBipedMain.aimedBow = false;
        //modelBipedMain.isSneak = false;
        //modelBipedMain.heldItemRight = 0;

        GL11.glPopMatrix();
    }
}