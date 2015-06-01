package joshie.harvest.core.helpers;

import java.util.Random;

import joshie.harvest.api.HFApi;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.EntityCropDigFX;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class DigFXHelper {
    private final static Random rand = new Random();

    public static void addBlockHitEffects(World world, int x, int y, int z, int side, EffectRenderer effect) {
        Block block = world.getBlock(x, y, z);
        IIcon icon = HFApi.CROPS.getCropAtLocation(world, x, y, z).getCropIcon(BlockCrop.getSection(world, x, y, z));
        if (block.getMaterial() != Material.air) {
            float f = 0.1F;
            double d0 = (double) x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinX();
            double d1 = (double) y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinY();
            double d2 = (double) z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinZ();

            if (side == 0) {
                d1 = (double) y + block.getBlockBoundsMinY() - (double) f;
            }

            if (side == 1) {
                d1 = (double) y + block.getBlockBoundsMaxY() + (double) f;
            }

            if (side == 2) {
                d2 = (double) z + block.getBlockBoundsMinZ() - (double) f;
            }

            if (side == 3) {
                d2 = (double) z + block.getBlockBoundsMaxZ() + (double) f;
            }

            if (side == 4) {
                d0 = (double) x + block.getBlockBoundsMinX() - (double) f;
            }

            if (side == 5) {
                d0 = (double) x + block.getBlockBoundsMaxX() + (double) f;
            }

            effect.addEffect((new EntityCropDigFX(icon, world, d0, d1, d2, x, y, z, block, world.getBlockMetadata(x, y, z))).applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
    }
}
