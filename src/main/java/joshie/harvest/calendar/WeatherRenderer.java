package joshie.harvest.calendar;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.handlers.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.IRenderHandler;

import org.lwjgl.opengl.GL11;

public class WeatherRenderer extends IRenderHandler {
    @Override
    public void render(float rain, WorldClient world, Minecraft mc) {
        Weather weather = DataHelper.getCalendar().getTodaysWeather();
        EntityRenderer renderer = mc.entityRenderer;
        float f1 = mc.theWorld.getRainStrength(rain);
        if (f1 > 0.0F) {
            renderer.enableLightmap((double) rain);

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

            EntityLivingBase entitylivingbase = mc.renderViewEntity;
            WorldClient worldclient = world;
            int k2 = MathHelper.floor_double(entitylivingbase.posX);
            int l2 = MathHelper.floor_double(entitylivingbase.posY);
            int i3 = MathHelper.floor_double(entitylivingbase.posZ);
            Tessellator tessellator = Tessellator.instance;
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            double d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double) rain;
            double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double) rain;
            double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double) rain;
            int k = MathHelper.floor_double(d1);
            byte b0 = 5;

            if (mc.gameSettings.fancyGraphics) {
                b0 = 10;
            }

            boolean flag = false;
            byte b1 = -1;
            float f5 = (float) renderer.rendererUpdateCount + rain;

            if (mc.gameSettings.fancyGraphics) {
                b0 = 10;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            flag = false;

            for (int l = i3 - b0; l <= i3 + b0; ++l) {
                for (int i1 = k2 - b0; i1 <= k2 + b0; ++i1) {
                    int j1 = (l - i3 + 16) * 32 + i1 - k2 + 16;
                    float f6 = renderer.rainXCoords[j1] * 0.5F;
                    float f7 = renderer.rainYCoords[j1] * 0.5F;
                    BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(i1, l);

                    if (biomegenbase.canSpawnLightningBolt() || biomegenbase.getEnableSnow()) {
                        int k1 = worldclient.getPrecipitationHeight(i1, l);
                        int l1 = l2 - b0;
                        int i2 = l2 + b0;

                        if (l1 < k1) {
                            l1 = k1;
                        }

                        if (i2 < k1) {
                            i2 = k1;
                        }

                        float f8 = 1.0F;
                        int j2 = k1;

                        if (k1 < k) {
                            j2 = k;
                        }

                        if (l1 != i2) {
                            renderer.random.setSeed((long) (i1 * i1 * 3121 + i1 * 45238971 ^ l * l * 418711 + l * 13761));
                            float f9 = biomegenbase.getFloatTemperature(i1, l1, l);
                            float f10;
                            double d4;

                            if (worldclient.getWorldChunkManager().getTemperatureAtHeight(f9, k1) >= 0.15F && (weather == Weather.RAIN || weather == Weather.TYPHOON)) {
                                if (b1 != 0) {
                                    if (b1 >= 0) {
                                        tessellator.draw();
                                    }

                                    b1 = 0;
                                    mc.getTextureManager().bindTexture(renderer.locationRainPng);
                                    tessellator.startDrawingQuads();
                                }

                                f10 = ((float) (renderer.rendererUpdateCount + i1 * i1 * 3121 + i1 * 45238971 + l * l * 418711 + l * 13761 & 31) + rain) / 32.0F * (3.0F + renderer.random.nextFloat());
                                double d3 = (double) ((float) i1 + 0.5F) - entitylivingbase.posX;
                                d4 = (double) ((float) l + 0.5F) - entitylivingbase.posZ;
                                float f12 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float) b0;
                                float f13 = 1.0F;
                                tessellator.setBrightness(worldclient.getLightBrightnessForSkyBlocks(i1, j2, l, 0));
                                tessellator.setColorRGBA_F(f13, f13, f13, ((1.0F - f12 * f12) * 0.5F + 0.5F) * f1);
                                tessellator.setTranslation(-d0 * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                                tessellator.addVertexWithUV((double) ((float) i1 - f6) + 0.5D, (double) l1, (double) ((float) l - f7) + 0.5D, (double) (0.0F * f8), (double) ((float) l1 * f8 / 4.0F + f10 * f8));
                                tessellator.addVertexWithUV((double) ((float) i1 + f6) + 0.5D, (double) l1, (double) ((float) l + f7) + 0.5D, (double) (1.0F * f8), (double) ((float) l1 * f8 / 4.0F + f10 * f8));
                                tessellator.addVertexWithUV((double) ((float) i1 + f6) + 0.5D, (double) i2, (double) ((float) l + f7) + 0.5D, (double) (1.0F * f8), (double) ((float) i2 * f8 / 4.0F + f10 * f8));
                                tessellator.addVertexWithUV((double) ((float) i1 - f6) + 0.5D, (double) i2, (double) ((float) l - f7) + 0.5D, (double) (0.0F * f8), (double) ((float) i2 * f8 / 4.0F + f10 * f8));
                                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                            } else {
                                if (b1 != 1) {
                                    if (b1 >= 0) {
                                        tessellator.draw();
                                    }

                                    b1 = 1;
                                    mc.getTextureManager().bindTexture(renderer.locationSnowPng);
                                    tessellator.startDrawingQuads();
                                }

                                f10 = ((float) (renderer.rendererUpdateCount & 511) + rain) / 512.0F;
                                float f16 = renderer.random.nextFloat() + f5 * 0.01F * (float) renderer.random.nextGaussian();
                                float f11 = renderer.random.nextFloat() + f5 * (float) renderer.random.nextGaussian() * 0.001F;
                                d4 = (double) ((float) i1 + 0.5F) - entitylivingbase.posX;
                                double d5 = (double) ((float) l + 0.5F) - entitylivingbase.posZ;
                                float f14 = MathHelper.sqrt_double(d4 * d4 + d5 * d5) / (float) b0;
                                float f15 = 1.0F;
                                tessellator.setBrightness((worldclient.getLightBrightnessForSkyBlocks(i1, j2, l, 0) * 3 + 15728880) / 4);
                                tessellator.setColorRGBA_F(f15, f15, f15, ((1.0F - f14 * f14) * 0.3F + 0.5F) * f1);
                                tessellator.setTranslation(-d0 * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                                tessellator.addVertexWithUV((double) ((float) i1 - f6) + 0.5D, (double) l1, (double) ((float) l - f7) + 0.5D, (double) (0.0F * f8 + f16), (double) ((float) l1 * f8 / 4.0F + f10 * f8 + f11));
                                tessellator.addVertexWithUV((double) ((float) i1 + f6) + 0.5D, (double) l1, (double) ((float) l + f7) + 0.5D, (double) (1.0F * f8 + f16), (double) ((float) l1 * f8 / 4.0F + f10 * f8 + f11));
                                tessellator.addVertexWithUV((double) ((float) i1 + f6) + 0.5D, (double) i2, (double) ((float) l + f7) + 0.5D, (double) (1.0F * f8 + f16), (double) ((float) i2 * f8 / 4.0F + f10 * f8 + f11));
                                tessellator.addVertexWithUV((double) ((float) i1 - f6) + 0.5D, (double) i2, (double) ((float) l - f7) + 0.5D, (double) (0.0F * f8 + f16), (double) ((float) i2 * f8 / 4.0F + f10 * f8 + f11));
                                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                            }
                        }
                    }
                }
            }

            if (b1 >= 0) {
                tessellator.draw();
            }

            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            renderer.disableLightmap((double) rain);
        }
    }
}
