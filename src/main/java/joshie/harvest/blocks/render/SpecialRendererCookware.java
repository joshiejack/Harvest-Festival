package joshie.harvest.blocks.render;

import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.init.HFBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SpecialRendererCookware extends TileEntitySpecialRenderer implements IItemRenderer {
    private final IModelCustom model;
    private final ResourceLocation resource;
    private final int meta;

    public SpecialRendererCookware(String name, int meta) {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(HFModInfo.MODPATH, "models/" + name + ".obj"));
        resource = new ResourceLocation(HFModInfo.MODPATH, "textures/models/" + name + ".png");
        this.meta = meta;
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HFBlocks.cookware), this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
        TileCooking cooking = (TileCooking) tile;
        if (cooking != null) {
            float scale = 1F;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.5F);
            switch (cooking.getFacing()) {
                case NORTH:
                    GL11.glRotatef(180F, 0, 1, 0);
                    break;
                case WEST:
                    GL11.glRotatef(-90F, 0, 1, 0);
                    break;
                case EAST:
                    GL11.glRotatef(90F, 0, 1, 0);
                    break;
                default:
                    break;
            }

            GL11.glScalef(scale, scale, scale);
            Minecraft.getMinecraft().renderEngine.bindTexture(resource);
            model.renderAll();
            GL11.glPopMatrix();
            renderCookware(cooking, x, y, z, tick);
        }
    }

    protected abstract void renderCookware(TileCooking cooking, double x, double y, double z, float tick);

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        switch (type) {
            case ENTITY:
                renderItem(0F, 0F, 0F, 1F);
                break;
            case EQUIPPED:
                renderItem(0, 0, 0.5F, 1F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderItem(+0.5F, 0.5F, +0.5F, 1F);
                break;
            case INVENTORY:
                renderItem(-0.5F, -0.25F, -0.5F, 1F);
                break;

            default:
                return;
        }
    }

    private void renderItem(float x, float y, float z, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(180F, 0F, 1F, 0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
        model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item.getItemDamage() == meta;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }
}
