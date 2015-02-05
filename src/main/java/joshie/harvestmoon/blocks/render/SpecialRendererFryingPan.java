package joshie.harvestmoon.blocks.render;

import java.util.ArrayList;

import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.lib.util.EntityFakeItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererFryingPan extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        TileFryingPan pan = (TileFryingPan) tile;
        if (pan != null) {
            ArrayList<ItemStack> ingredients = pan.getIngredients();
            ArrayList<ItemStack> seasonings = pan.getSeasonings();
            ItemStack result = pan.getStored();
            GL11.glPushMatrix();
            float offsetX = (float) (x - 0.2F);
            float offsetY = (float) (y - 0F);
            float offsetZ = (float) (z - 0.2F);
            GL11.glTranslated(x, y, z);
            //GL11.glScalef(0.7F, 0.7F, 0.7F);
            if (result != null) {
                renderResult(pan.getWorldObj(), result);
            }

            int max = ingredients.size();
            for (int i = 0; i < max; i++) {
                renderIngredient(tile.getWorldObj(), ingredients.get(i), max, i, pan.getRotation());
            }

            GL11.glPopMatrix();
        }
    }

    void renderIngredient(World world, ItemStack stack, int max, int id, float rotation) {
        EntityFakeItem entityitem = new EntityFakeItem(world, 0.0D, 0.0D, 0.0D, stack);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, -0.125F, 0.5F);
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

    void renderResult(World world, ItemStack stack) {
        EntityFakeItem entityitem = new EntityFakeItem(world, 0.0D, 0.0D, 0.0D, stack);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.125F, 0.625F);
        if (!(stack.getItem() instanceof ItemBlock)) {
            GL11.glRotatef(-90, 1F, 0F, 0F);
        } else {
            GL11.glRotatef(90, 0F, 1F, 0F);
        }

        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();
    }
}
