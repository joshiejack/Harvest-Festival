package joshie.harvestmoon.handlers;

import java.util.HashMap;

import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.crops.render.RenderCrop;
import joshie.harvestmoon.helpers.ClientHelper;
import joshie.harvestmoon.lib.RenderIds;
import joshie.harvestmoon.network.PacketCropRequest;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.util.RenderBase;
import joshie.harvestmoon.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderHandler implements ISimpleBlockRenderingHandler {
    private static final HashMap<String, RenderCrop> crops = new HashMap();
    private static final HashMap<String, RenderBase> renders = new HashMap();

    public static void registerCrop(String string, Class clazz) {
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
            String name = ClientHelper.getCropTracker().getCropName(joshie.harvestmoon.helpers.generic.MCClientHelper.getWorld(), x, y, z);
            if (name != null) {
                RenderBase render = crops.get(name);
                if (render != null) {
                    return render.render(renderer, world, x, y, z);
                } else return false;
            } else {
                PacketHandler.sendToServer(new PacketCropRequest(joshie.harvestmoon.helpers.generic.MCClientHelper.getWorld(), x, y, z));
                return false;
            }
        } else {
            String data = Block.blockRegistry.getNameForObject(block) + ":" + world.getBlockMetadata(x, y, z);
            if (renders.get(data) != null) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof IFaceable) {
                    return renders.get(data).setFacing(((IFaceable) tile).getFacing()).render(renderer, world, x, y, z);
                } else return renders.get(data).render(renderer, world, x, y, z);
            } else return false;
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
