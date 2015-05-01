package joshie.harvestmoon.items.render;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.blocks.PlaceableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderBuilding implements IItemRenderer {
    public static RenderBlocks renderer = new RenderBlocks();
    
    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        GL11.glPushMatrix();
        float scale = 0.15F;
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(-1F, -2F, -1F);
        Building building = Building.buildings.get(stack.getItemDamage());
        for (Placeable placeable : building.getList()) {
            if (placeable instanceof PlaceableBlock) {
                PlaceableBlock block = (PlaceableBlock) placeable;
                int xCoord = block.getX();
                int yCoord = block.getY();
                int zCoord = block.getZ();
                if (yCoord < 2) continue;
                
                GL11.glPushMatrix();
                GL11.glTranslatef(xCoord, yCoord, zCoord);
                renderer.renderBlockAsItem(block.getBlock(), block.getMetaData(false, false, false), 1F);
                GL11.glPopMatrix();
            }
        }
        
        GL11.glPopMatrix();
    }
}
