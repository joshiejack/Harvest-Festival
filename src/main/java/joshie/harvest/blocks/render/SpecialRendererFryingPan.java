package joshie.harvest.blocks.render;

import java.util.ArrayList;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.core.util.generic.EntityFakeItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererFryingPan extends SpecialRendererCookware {
    public SpecialRendererFryingPan() {
        super("frying_pan", BlockCookware.FRYING_PAN);
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
            Fluid fluid = HFApi.COOKING.getFluid(ingredient);
            if (fluid != null) {
                renderFluid(fluid);
            } else renderIngredient(tile.getWorldObj(), ingredient, max, i, tile.getRotation());
        }

        GL11.glPopMatrix();
    }

    void renderIngredient(World world, ItemStack stack, int max, int id, float rotation) {
        EntityFakeItem entityitem = new EntityFakeItem(world, 0.0D, 0.0D, 0.0D, stack);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, -0.1F, 0.5F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        if (!(stack.getItem() instanceof ItemBlock)) {
            GL11.glRotatef(-90, 1F, 0F, 0F);
            if (id < 8) {
                GL11.glRotatef((id * 45) + rotation, 0F, 0F, 1F);
                GL11.glTranslatef(0.5F, 0F, 0.5F);
            } else {
                GL11.glTranslatef(0.0F, -0.1F, 0.5F);
            }
        } else {
            GL11.glRotatef(90, 0F, 1F, 0F);
        }

        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();
    }

    private void renderFluid(Fluid fluid) {

    }

    void renderResult(World world, ItemStack stack) {
        EntityFakeItem entityitem = new EntityFakeItem(world, 0.0D, 0.0D, 0.0D, stack);
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
