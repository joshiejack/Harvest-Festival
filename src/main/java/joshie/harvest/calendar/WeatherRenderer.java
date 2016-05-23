package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.IRenderHandler;

import java.util.Random;

public class WeatherRenderer extends IRenderHandler {
    public static void addRainParticles(Minecraft mc, Random random, EntityRenderer renderer) {
        float f = mc.theWorld.getRainStrength(1.0F);

        if (!mc.gameSettings.fancyGraphics) {
            f /= 2.0F;
        }

        if (f > 0.0F) {
            random.setSeed((long) renderer.rendererUpdateCount * 312987231L);
            Entity entity = mc.getRenderViewEntity();
            WorldClient world = mc.theWorld;
            BlockPos pos = new BlockPos(entity);
            int i = 10;
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

            Weather weather = HFTrackers.getCalendar(world).getTodaysWeather();
            for (int l = 0; l < k; ++l) {
                BlockPos pos1 = world.getPrecipitationHeight(pos.add(random.nextInt(i) - random.nextInt(i), 0, random.nextInt(i) - random.nextInt(i)));

                Biome biome = world.getBiomeGenForCoords(pos1);
                BlockPos pos2 = pos1.down();
                IBlockState state = world.getBlockState(pos2);

                if (pos1.getY() <= pos.getY() + i && pos1.getY() >= pos.getY() - i && biome.canRain() && biome.getFloatTemperature(pos1) >= 0.15F && !weather.isSnow()) {
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    AxisAlignedBB aabb = state.getBoundingBox(world, pos2);

                    if (state.getMaterial() == Material.LAVA) {
                        mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) pos1.getX() + d3, (double) ((float) pos1.getY() + 0.1F) - aabb.minY, (double) pos1.getZ() + d4, 0.0D, 0.0D, 0.0D);
                    } else if (state.getMaterial() != Material.AIR) {
                        ++j;

                        if (random.nextInt(j) == 0) {
                            d0 = (double) pos2.getX() + d3;
                            d1 = (double) ((float) pos2.getY() + 0.1F) + aabb.maxY - 1.0D;
                            d2 = (double) pos2.getZ() + d4;
                        }

                        mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double) pos2.getX() + d3, (double) ((float) pos2.getY() + 0.1F) + aabb.maxY, (double) pos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
            }

            if (j > 0 && random.nextInt(3) < renderer.rainSoundCounter++) {
                renderer.rainSoundCounter = 0;

                if (d1 > (double) (pos.getY() + 1) && world.getPrecipitationHeight(pos).getY() > MathHelper.floor_float((float) pos.getY())) {
                    mc.theWorld.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN_ABOVE, SoundCategory.WEATHER, 0.1F, 0.5F, false);
                } else {
                    mc.theWorld.playSound(d0, d1, d2, SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }

    @Override
    public void render(float rain, WorldClient world, Minecraft mc) {
        Weather weather = HFTrackers.getCalendar(world).getTodaysWeather();
        EntityRenderer renderer = mc.entityRenderer;
        float f = mc.theWorld.getRainStrength(rain);
        if (f > 0.0F) {
            renderer.enableLightmap();

            if (renderer.rainXCoords == null) {
                renderer.rainXCoords = new float[1024];
                renderer.rainYCoords = new float[1024];

                for (int i = 0; i < 32; ++i) {
                    for (int j = 0; j < 32; ++j) {
                        float f2 = (float) (j - 16);
                        float f3 = (float) (i - 16);
                        float f4 = MathHelper.sqrt_float(f2 * f2 + f3 * f3);
                        renderer.rainXCoords[i << 5 | j] = -f3 / f4;
                        renderer.rainYCoords[i << 5 | j] = f2 / f4;
                    }
                }
            }

            Entity entity = mc.getRenderViewEntity();
            int i = MathHelper.floor_double(entity.posX);
            int j = MathHelper.floor_double(entity.posY);
            int k = MathHelper.floor_double(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            GlStateManager.disableCull();
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc(516, 0.1F);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) rain;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) rain;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) rain;
            int l = MathHelper.floor_double(d1);
            int i1 = 5;

            if (mc.gameSettings.fancyGraphics) {
                i1 = 10;
            }

            int j1 = -1;
            float f1 = (float) renderer.rendererUpdateCount + rain;
            buffer.setTranslation(-d0, -d1, -d2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = renderer.rainXCoords[i2] * 0.5F;
                    double d4 = renderer.rainYCoords[i2] * 0.5F;
                    Biome biome = world.getBiomeGenForCoords(mutablePos);

                    if (biome.canRain() || biome.getEnableSnow()) {
                        int j2 = world.getPrecipitationHeight(mutablePos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;

                        if (k2 < j2) {
                            k2 = j2;
                        }

                        if (l2 < j2) {
                            l2 = j2;
                        }

                        int i3 = j2;

                        if (j2 < l) {
                            i3 = l;
                        }

                        if (k2 != l2) {
                            renderer.random.setSeed((long) (l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                            mutablePos.setPos(l1, k2, k1);
                            float f2 = biome.getFloatTemperature(mutablePos);

                            if (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15F && !weather.isSnow()) {
                                if (j1 != 0) {
                                    if (j1 >= 0) {
                                        tessellator.draw();
                                    }
                                    j1 = 0;
                                    mc.getTextureManager().bindTexture(EntityRenderer.RAIN_TEXTURES);
                                    buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d5 = -((double) (renderer.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double) rain) / 32.0D * (3.0D + renderer.random.nextDouble());
                                double d6 = (double) ((float) l1 + 0.5F) - entity.posX;
                                double d7 = (double) ((float) k1 + 0.5F) - entity.posZ;

                                float f3 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float) i1;
                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                                mutablePos.setPos(l1, i3, k1);
                                int j3 = world.getCombinedLight(mutablePos, 0);
                                int k3 = j3 >> 16 & 65535;
                                int l3 = j3 & 65535;

                                buffer.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D).tex(0.0D, (double) k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                                buffer.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D).tex(1.0D, (double) k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                                buffer.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D).tex(1.0D, (double) l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                                buffer.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D).tex(0.0D, (double) l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                            } else {
                                if (j1 != 1) {
                                    if (j1 >= 0) {
                                        tessellator.draw();
                                    }

                                    j1 = 1;
                                    mc.getTextureManager().bindTexture(EntityRenderer.SNOW_TEXTURES);
                                    buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d8 = (double) (-((float) (renderer.rendererUpdateCount & 511) + rain) / 512.0F);
                                double d9 = renderer.random.nextDouble() + (double) f1 * 0.01D * (double) ((float) renderer.random.nextGaussian());
                                double d10 = renderer.random.nextDouble() + (double) (f1 * (float) renderer.random.nextGaussian()) * 0.001D;
                                double d11 = (double) ((float) l1 + 0.5F) - entity.posX;
                                double d12 = (double) ((float) k1 + 0.5F) - entity.posZ;
                                float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / (float) i1;
                                float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
                                mutablePos.setPos(l1, i3, k1);
                                int i4 = (world.getCombinedLight(mutablePos, 0) * 3 + 15728880) / 4;
                                int j4 = i4 >> 16 & 65535;
                                int k4 = i4 & 65535;

                                buffer.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D).tex(0.0D + d9, (double) k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                                buffer.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D).tex(1.0D + d9, (double) k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                                buffer.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D).tex(1.0D + d9, (double) l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                                buffer.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D).tex(0.0D + d9, (double) l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                            }
                        }
                    }
                }
            }

            if (j1 >= 0) {
                tessellator.draw();
            }

            buffer.setTranslation(0.0D, 0.0D, 0.0D);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            renderer.disableLightmap();
        }
    }
}