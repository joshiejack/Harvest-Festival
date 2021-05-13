package uk.joshiejack.seasons.client.renderer;

import uk.joshiejack.penguinlib.util.helpers.minecraft.WorldHelper;
import uk.joshiejack.seasons.client.WorldDataClient;
import uk.joshiejack.seasons.world.weather.Weather;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FogRenderer {
    private static final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    private static int fogStart = 0;
    private static int fogTarget = 0;

    @SubscribeEvent
    public static void onFogRender(EntityViewRenderEvent.RenderFogEvent event) {
        if (event.getEntity().world.provider.getDimension() == 0) {
            if (!event.getState().getMaterial().isLiquid()) {
                //Update the fog smoothly
                if (fogTarget != fogStart) {
                    if (fogTarget > fogStart) {
                        fogStart += 5;
                    } else if (fogTarget < fogStart) {
                        fogStart -= 5;
                    }
                }

                //GlStateManager.enableLighting();
                Minecraft mc = Minecraft.getMinecraft();
                blockpos$mutableblockpos.setPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                int i1 = mc.gameSettings.fancyGraphics ? 10 : 5;
                int j = MathHelper.floor(mc.player.posY);
                int j2 = mc.world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                int k2 = j - i1;
                int l2 = j + i1;

                if (k2 < j2) {
                    k2 = j2;
                }

                if (l2 < j2) {
                    l2 = j2;
                }


                Weather weather = WorldDataClient.INSTANCE.getWeather();
                boolean isSnow = (weather == Weather.RAIN || weather == Weather.STORM) && WorldHelper.snows(mc.world, blockpos$mutableblockpos);
                if (k2 != l2) {
                    if (weather == Weather.FOGGY || isSnow) {
                        switch (weather) {
                            case STORM:
                                fogTarget = -5000;
                                break;
                            case RAIN:
                                fogTarget = -100;
                                break;
                            default:
                                fogTarget = 50;
                                break;
                        }
                    } else fogTarget = 5000;
                } else fogTarget = 100;
                if (blockpos$mutableblockpos.getY() < j2) fogTarget = 5000;

                //If we're snow or resetting the target
                if (isSnow || weather == Weather.FOGGY) {
                    GlStateManager.setFogEnd(Math.min(event.getFarPlaneDistance(), 150F) * 0.5F);
                    GlStateManager.setFogStart((float) fogStart / 100F);
                }
            } else {
                fogStart = 100;
                fogTarget = 100;
            }
        }
    }

    @SubscribeEvent
    public static void onFogColor(EntityViewRenderEvent.FogColors event) {
        if (event.getEntity().world.provider.getDimension() == 0) {
            if (!event.getState().getMaterial().isLiquid()) {
                Weather weather = WorldDataClient.INSTANCE.getWeather();
                Minecraft mc = Minecraft.getMinecraft();
                blockpos$mutableblockpos.setPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                boolean isSnow = (weather == Weather.RAIN || weather == Weather.STORM) && WorldHelper.snows(mc.world, blockpos$mutableblockpos);
                if (isSnow) {
                    event.setRed(1F);
                    event.setBlue(1F);
                    event.setGreen(1F);
                } else if (weather == Weather.FOGGY) {
                    event.setRed(0.55F);
                    event.setBlue(0.55F);
                    event.setGreen(0.55F);
                }
            }
        }
    }
}
