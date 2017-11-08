package joshie.harvest.asm;

import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.core.proxy.HFClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

@SuppressWarnings("unused")
public class RenderHook {
    /**
     * Added at the start of renderByItem to render my special renderers
     **/
    public static boolean render(ItemStack stack) {
        EntityItemRenderer tile = HFClientProxy.RENDER_MAP.get(stack.getItem());
        if (tile == null) return false;
        else {
            tile.setStack(stack); //Set the id and render the tile
            TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, 0.0D, 0.0D, 0.0D, 0.0F);
            return true;
        }
    }

    /** Replaces the vanilla addRainParticles so that I can
     *  Remove rain splashes when it's snowing and not raining
     *  Add rain sound when it's raining in snowy biomes */
    @SuppressWarnings("unused")
    public static void addRainParticles(Minecraft mc, EntityRenderer renderer, Random random) {
        float f = mc.world.getRainStrength(1.0F);

        if (!mc.gameSettings.fancyGraphics) {
            f /= 2.0F;
        }

        if (f != 0.0F) {
            random.setSeed((long) renderer.rendererUpdateCount * 312987231L);
            Entity entity = mc.getRenderViewEntity();
            World world = mc.world;
            BlockPos blockpos = new BlockPos(entity);
            double d0 = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            int j = 0;
            int k = (int) (100.0F * f * f);

            if (mc.gameSettings.particleSetting == 1) {
                k >>= 1;
            } else if (mc.gameSettings.particleSetting == 2) {
                k = 0;
            }

            for (int l = 0; l < k; ++l) {
                BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(random.nextInt(10) - random.nextInt(10), 0, random.nextInt(10) - random.nextInt(10)));
                Biome biome = world.getBiome(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                IBlockState iblockstate = world.getBlockState(blockpos2);

                if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10 && biome.canRain()) {
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, blockpos2);

                    if (iblockstate.getMaterial() != Material.LAVA && iblockstate.getBlock() != Blocks.MAGMA) {
                        if (iblockstate.getMaterial() != Material.AIR) {
                            ++j;

                            if (random.nextInt(j) == 0) {
                                d0 = (double) blockpos2.getX() + d3;
                                d1 = (double) ((float) blockpos2.getY() + 0.1F) + axisalignedbb.maxY - 1.0D;
                                d2 = (double) blockpos2.getZ() + d4;
                            }

                            mc.world.spawnParticle(EnumParticleTypes.WATER_DROP, (double) blockpos2.getX() + d3, (double) ((float) blockpos2.getY() + 0.1F) + axisalignedbb.maxY, (double) blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D);
                        }
                    } else {
                        mc.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) blockpos1.getX() + d3, (double) ((float) blockpos1.getY() + 0.1F) - axisalignedbb.minY, (double) blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D);
                    }
                }
            }

            if (j > 0 && random.nextInt(3) < renderer.rainSoundCounter++) {
                renderer.rainSoundCounter = 0;
                if (d1 > (double) (blockpos.getY() + 1)) {
                    mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
                } else {
                    mc.world.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }
}
