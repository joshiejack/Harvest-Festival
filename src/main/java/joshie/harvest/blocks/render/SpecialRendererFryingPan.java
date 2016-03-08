package joshie.harvest.blocks.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.cooking.entity.EntityCookingItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererFryingPan extends SpecialRendererCookware {
    public SpecialRendererFryingPan() {
        super("ModelFryingPan", BlockCookware.FRYING_PAN);
    }

    @Override
    public void renderCookware(TileCooking tile, double x, double y, double z, float tick) {
        ArrayList<ItemStack> ingredients = tile.getIngredients();
        ItemStack result = tile.getResult();
        GL11.glPushMatrix();
        float offsetX = (float) (x - 0.2F);
        float offsetY = (float) (y - 0F);
        float offsetZ = (float) (z - 0.2F);
        GL11.glTranslated(x, y, z);
        //GL11.glScalef(0.7F, 0.7F, 0.7F);
        if (result != null) {
            renderResult(tile.getWorldObj(), result);
        }

        int max = ingredients.size();
        for (int i = 0; i < max; i++) {
            ItemStack ingredient = ingredients.get(i);
            if (HFApi.COOKING.getFluid(ingredient) == null) {
                renderIngredient(tile.getWorldObj(), ingredient, max, tile.heightOffset[i], tile.rotations[i], tile.offset1[i], tile.offset2[i]);
            }
        }

        GL11.glPopMatrix();
    }

    void renderIngredient(World world, ItemStack stack, int max, float pos, float rotation, float offset1, float offset2) {
        EntityCookingItem entityitem = new EntityCookingItem(world, 0.0D, 0.0D, 0.0D, stack, false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, -0.1F, 0.5F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        if (!(stack.getItem() instanceof ItemBlock)) {
            GL11.glRotatef(-90, 1F, 0F, 0F);
            GL11.glRotatef(rotation, 0F, 0F, 1F);
            GL11.glTranslatef(offset1, offset2, pos);
        } else {
            GL11.glRotatef(90, 1F, 0F, 0F);
            GL11.glTranslatef(offset1 * 1.4F, 0.8F - offset2 * 2.5F, pos - 1F);
        }

        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();
    }

    public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5, int brightness) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(brightness);
        tessellator.addVertexWithUV(par1 + 0, par2 + par5, 0, par3Icon.getMinU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + par5, 0, par3Icon.getMaxU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + 0, 0, par3Icon.getMaxU(), par3Icon.getMinV());
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, par3Icon.getMinU(), par3Icon.getMinV());
        tessellator.draw();
    }

    void renderResult(World world, ItemStack stack) {
        EntityCookingItem entityitem = new EntityCookingItem(world, 0.0D, 0.0D, 0.0D, stack, true);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.15F, 0.625F);
        if (!(stack.getItem() instanceof ItemBlock)) {
            GL11.glRotatef(-90, 1F, 0F, 0F);
        } else {
            GL11.glRotatef(90, 0F, 1F, 0F);
        }

        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();
    }
}
