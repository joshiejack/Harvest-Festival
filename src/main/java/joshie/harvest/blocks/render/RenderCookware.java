package joshie.harvest.blocks.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.blocks.items.ItemBlockCookware;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.init.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderCookware implements ISimpleBlockRenderingHandler {
    private static final TIntObjectMap<IModelCustom> models = new TIntObjectHashMap<IModelCustom>();
    private static final TIntObjectMap<ResourceLocation> resources = new TIntObjectHashMap<ResourceLocation>();

    public RenderCookware() {
        ItemBlockCookware item = (ItemBlockCookware) Item.getItemFromBlock(HFBlocks.cookware);
        for (int i = 1; i < 8; i++) {
            String name = item.getName(new ItemStack(item, 1, i)).replace(".", "_");
            try {
                models.put(i, AdvancedModelLoader.loadModel(new ResourceLocation(HFModInfo.MODPATH, "models/" + name + ".obj")));
                resources.put(i, new ResourceLocation(HFModInfo.MODPATH, "textures/models/" + name + ".png"));
            } catch (Exception e) {}
        }
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int id, RenderBlocks render) {
        ResourceLocation resource = resources.get(meta);
        if (resource != null) {
            GL11.glPushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(resource);
            models.get(meta).renderAll();
            GL11.glPopMatrix();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int id, RenderBlocks render) {
        int meta = world.getBlockMetadata(x, y, z);
        ResourceLocation resource = resources.get(meta);
        if (resource != null) {
            // Stop the tesselator from messing with us
            int previousDrawMode = Tessellator.instance.drawMode;
            GL11.glPushMatrix();
            Tessellator.instance.draw();
            TileEntity tile = world.getTileEntity(x, y, z);
            ForgeDirection dir = ForgeDirection.NORTH;
            if (tile instanceof TileCooking) {
                dir = ((TileCooking) tile).getFacing();
            }

            GL11.glPushMatrix();
            int chunkX = x % 16;
            int chunkZ = z % 16;

            double offsetX = 0D;
            double offsetZ = 0D;
            //Rotate and Fix Translations
            if (dir == ForgeDirection.SOUTH) {
                GL11.glTranslatef(chunkX, 1F, chunkZ);
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-chunkX, -1F, -chunkZ);
                offsetX = -1D;
                offsetZ = -1D;
            } else if (dir == ForgeDirection.EAST) {
                GL11.glTranslatef(chunkX, 0F, chunkZ);
                GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-chunkX, 0F, -chunkZ);
                offsetZ = -1D;
            } else if (dir == ForgeDirection.WEST) {
                GL11.glTranslatef(chunkX, 0F, chunkZ);
                GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-chunkX, 0F, -chunkZ);
                offsetX = -1D;
            }

            //Fix X and Z Offsets
            if (chunkX != 0 && x <= 0 && dir == ForgeDirection.SOUTH) {
                offsetX -= 32;
            }

            if (chunkX != 0 && x <= 0 && dir == ForgeDirection.WEST) {
                offsetX -= 16;
                offsetZ += 16;
            }

            if (chunkX != 0 && x <= 0 && dir == ForgeDirection.EAST) {
                offsetX -= 16;
                offsetZ -= 16;
            }

            if (chunkZ != 0 && z <= 0 && dir == ForgeDirection.SOUTH) {
                offsetZ -= 32;
            }

            if (chunkZ != 0 && z <= 0 && dir == ForgeDirection.WEST) {
                offsetZ -= 16;
                offsetX -= 16;
            }

            if (chunkZ != 0 && z <= 0 && dir == ForgeDirection.EAST) {
                offsetX += 16;
                offsetZ -= 16;
            }

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glTranslated(x + offsetX, y, z + offsetZ);
            GL11.glColor4f(1F, 1F, 1F, 1F);

            Minecraft.getMinecraft().renderEngine.bindTexture(resource);
            models.get(meta).renderAll();
            GL11.glPopMatrix();

            // Restart the Tesselator
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            Tessellator.instance.startDrawing(previousDrawMode);
            GL11.glPopMatrix();
            return true;
        } else return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int id) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RenderIds.COOKING;
    }

}