package joshie.harvestmoon.crops.icons;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IconHandlerBlock extends AbstractIconHandler {
    private Block block;

    public IconHandlerBlock(Block block) {
        this.block = block;
    }

    @Override
    public boolean doCustomRender(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, Block block) {
        int stage = HMApi.CROPS.getCropAtLocation(MCClientHelper.getWorld(), x, y, z).getStage();
        BlockStem blockstem = (BlockStem) Blocks.melon_stem;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(blockstem.getMixedBrightnessForBlock(world, x, y, z));
        int l = blockstem.colorMultiplier(world, x, y, z);
        float f = (float) (l >> 16 & 255) / 255.0F;
        float f1 = (float) (l >> 8 & 255) / 255.0F;
        float f2 = (float) (l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        int meta = getMetaDataFromStage(stage);
        tessellator.setColorOpaque_F(f, f1, f2);
        double maxY = (double)((float)(meta * 2 + 2) / 16.0F);
        float f3 = 0.125F;
        block.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, (float)maxY, 0.5F + f);
        renderer.setRenderBoundsFromBlock(block);
        
        int stemState = -1;
        if (stage == 15) {
            if (world.getBlock(x - 1, y, z) == this.block) {
                stemState = 0;
            } else if (world.getBlock(x + 1, y, z) == this.block) {
                stemState = 1;
            } else if (world.getBlock(x, y, z - 1) == this.block) {
                stemState = 2;
            } else if (world.getBlock(x, y, z + 1) == this.block) {
                stemState = 3;
            }
        }
                
        if (stemState < 0) {
            renderer.renderBlockStemSmall(blockstem, meta, renderer.renderMaxY, (double) x, (double) ((float) y - 0.0625F), (double) z);
        } else {
            renderer.renderBlockStemSmall(blockstem, 7, 0.5D, (double) x, (double) ((float) y - 0.0625F), (double) z);
            renderer.renderBlockStemBig(blockstem, 7, stemState, renderer.renderMaxY, (double) x, (double) ((float) y - 0.0625F), (double) z);
        }

        return true;
    }

    private int getMetaDataFromStage(int stage) {
        if (stage == 0) {
            return 0;
        } else if (stage <= 2) {
            return 1;
        } else if (stage <= 4) {
            return 2;
        } else if (stage <= 8) {
            return 3;
        } else if (stage <= 12) {
            return 4;
        } else if (stage <= 14) {
            return 5;
        } else {
            return 6;
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconForStage(PlantSection section, int stage) {
        return Blocks.carrots.getIcon(0, 0);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {}
}
