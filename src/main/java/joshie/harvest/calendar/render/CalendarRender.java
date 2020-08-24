package joshie.harvest.calendar.render;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.npcs.gui.GuiNPCBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@HFEvents(Side.CLIENT)
@SuppressWarnings("unused")
public class CalendarRender {
    private static final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    public static volatile TIntIntMap grassToBlend = new TIntIntHashMap();
    public static volatile TIntIntMap leavesToBlend = new TIntIntHashMap();
    private static int fogStart = 0;
    private static int fogTarget = 0;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiNPCBase) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onFogRender(RenderFogEvent event) {
        if (HFCalendar.ENABLE_SNOW_FOG && event.getEntity().world.provider.getDimension() == 0) {
            if (!event.getState().getMaterial().isLiquid()) {
                //Update the fog smoothly
                if (fogTarget != fogStart) {
                    if (fogTarget > fogStart) {
                        fogStart += 5;
                    } else if (fogTarget < fogStart) {
                        fogStart -= 5;
                    }
                }

                Minecraft mc = MCClientHelper.getMinecraft();
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

                Weather weather = HFApi.calendar.getWeather(mc.world);
                if (k2 != l2) {
                    Biome biome = mc.world.getBiome(blockpos$mutableblockpos);
                    if (biome.canRain() && !biome.isHighHumidity()) {
                        if (weather == Weather.BLIZZARD) {
                            fogTarget = -20000;
                        } else if (weather == Weather.SNOW) {
                            fogTarget = -1000;
                        } else fogTarget = 100;
                    } else fogTarget = 5000;
                } else fogTarget = 100;
                if (blockpos$mutableblockpos.getY() < j2) fogTarget = 5000;

                //If we're snow or resetting the target
                if (weather.isSnow()) {
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
    public void onFogColor(FogColors event) {
        if (HFCalendar.ENABLE_SNOW_FOG && event.getEntity().world.provider.getDimension() == 0) {
            if (!event.getState().getMaterial().isLiquid()) {
                Weather weather = HFApi.calendar.getWeather(MCClientHelper.getWorld());
                if (weather != null && (weather == Weather.SNOW || weather == Weather.BLIZZARD)) {
                    event.setRed(1F);
                    event.setBlue(1F);
                    event.setGreen(1F);
                }
            }
        }
    }

    @SubscribeEvent
    public void getFoliageColor(GetFoliageColor event) {
        if (!event.getBiome().canRain() || event.getBiome().isHighHumidity()) return;
        if (HFApi.calendar.getDate(MCClientHelper.getWorld()).getSeason() == Season.AUTUMN) {
            event.setNewColor(0xFF9900);
        } else {
            int leaves = HFTrackers.getCalendar(MCClientHelper.getWorld()).getSeasonData().leavesColor;
            if (leaves != 0) {
                event.setNewColor(CalendarHelper.getBlendedColour(leavesToBlend, event.getOriginalColor(), leaves));
            }
        }
    }

    @SubscribeEvent
    public void getGrassColor(GetGrassColor event) {
        if (!event.getBiome().canRain() || event.getBiome().isHighHumidity()) return;
        int grass = HFTrackers.getCalendar(MCClientHelper.getWorld()).getSeasonData().grassColor;
        if (grass != 0) {
            event.setNewColor(CalendarHelper.getBlendedColour(grassToBlend, event.getOriginalColor(), grass));
        }
    }
}