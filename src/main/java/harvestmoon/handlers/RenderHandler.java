package harvestmoon.handlers;

import static harvestmoon.HarvestMoon.handler;
import harvestmoon.blocks.BlockCrop;
import harvestmoon.crops.render.RenderCrop;
import harvestmoon.helpers.ClientHelper;
import harvestmoon.lib.RenderIds;
import harvestmoon.network.PacketCropRequest;
import harvestmoon.network.PacketHandler;
import harvestmoon.util.RenderBase;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderHandler implements ISimpleBlockRenderingHandler {
    private static final HashMap<String, RenderCrop> crops = new HashMap();
    private static final HashMap<String, RenderBase> renders = new HashMap();

    public static void register(String string, Class clazz) {
        try {
            crops.put(string, (RenderCrop) clazz.newInstance());
        } catch (Exception e) {}
    }

    public static void register(Block block, int meta, Class clazz) {
        try {
            renders.put(Block.blockRegistry.getNameForObject(block) + ":" + meta, (RenderBase) clazz.newInstance());
        } catch (Exception e) {}
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {
        String data = Block.blockRegistry.getNameForObject(block) + ":" + meta;
        if (renders.get(data) != null) {
            renders.get(data).render(renderer, block);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (block instanceof BlockCrop) {
            String name = handler.getClient().getCropTracker().getCropName(ClientHelper.getWorld(), x, y, z);
            if (name != null) {
                RenderBase render = crops.get(name);
                if (render != null) {
                    return render.render(renderer, world, x, y, z);
                } else return false;
            } else {
                PacketHandler.sendToServer(new PacketCropRequest(ClientHelper.getWorld(), x, y, z));
                return false;
            }
        } else {

            String data = Block.blockRegistry.getNameForObject(block) + ":" + world.getBlockMetadata(x, y, z);
            if (renders.get(data) != null) return renders.get(data).render(renderer, world, x, y, z);
            else return false;
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RenderIds.ALL;
    }
}
