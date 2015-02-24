package joshie.harvestmoon.items.render;

import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderSeedBag implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    public void renderItem(ItemStack stack, int pass) {
        TextureManager texturemanager = MCClientHelper.getMinecraft().getTextureManager();
        Item item = stack.getItem();
        int damage = stack.getItemDamage();

        IIcon iicon = item.getIconFromDamageForRenderPass(damage, pass);
        if (iicon == null) {
            GL11.glPopMatrix();
            return;
        }

        GL11.glPushMatrix();
        if (pass != 3) {
            GL11.glScalef(15F, 15F, 1F);
            GL11.glRotatef(180, 1F, 0F, 0F);
            GL11.glRotatef(180, 0F, 1F, 0F);
            GL11.glTranslatef(-1.025F, -1.025F, 0F);
        } else {
            GL11.glScalef(6.5F, 6.5F, 6.5F);
            GL11.glRotatef(180, 1F, 0F, 0F);
            GL11.glRotatef(180, 0F, 1F, 0F);
            GL11.glTranslatef(-1.65F, -2F, 0.1F);
        }

        texturemanager.bindTexture(texturemanager.getResourceLocation(stack.getItemSpriteNumber()));
        TextureUtil.func_152777_a(false, false, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        float f = iicon.getMinU();
        float f1 = iicon.getMaxU();
        float f2 = iicon.getMinV();
        float f3 = iicon.getMaxV();
        float f4 = 0.0F;
        float f5 = 0.3F;

        GL11.glEnable(GL11.GL_BLEND);
        ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
        GL11.glDisable(GL11.GL_BLEND);
        TextureUtil.func_147945_b();
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        GL11.glPushMatrix();
        if (type == ItemRenderType.EQUIPPED) {
            GL11.glScalef(0.075F, 0.075F, 0.075F);
            GL11.glRotatef(115, 1F, 0F, 0F);
            GL11.glRotatef(180, 0F, 1F, 0F);
            GL11.glRotatef(5, 0F, 0F, 1F);
            GL11.glTranslatef(-15F, -1F, 0F);
        } else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glScalef(0.075F, 0.075F, 0.075F);
            GL11.glRotatef(180, 0F, 1F, 0F);
            GL11.glTranslatef(-15F, -1F, 0F);
        } else if (type == ItemRenderType.ENTITY) {
            EntityItem item = (EntityItem) data[1];
            GL11.glScalef(0.075F, 0.075F, 0.075F);
            GL11.glRotatef(180, 0F, 1F, 0F);
            GL11.glRotatef(180, 1F, 0F, 0F);
            GL11.glTranslatef(-8F, -11.5F, -3.5F);

            GL11.glRotatef(item.rotationPitch, 0F, 1F, 0F);
            item.rotationPitch += 0.5F;
        }

        for (int x = 1; x <= stack.getItem().getRenderPasses(stack.getItemDamage()); x++) {
            int k1 = stack.getItem().getColorFromItemStack(stack, x);
            float f10 = (float) (k1 >> 16 & 255) / 255.0F;
            float f11 = (float) (k1 >> 8 & 255) / 255.0F;
            float f12 = (float) (k1 & 255) / 255.0F;
            GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);
            renderItem(stack, x);
        }

        GL11.glPopMatrix();
    }
}
