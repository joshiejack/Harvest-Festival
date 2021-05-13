package uk.joshiejack.energy.client;

import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyFoodStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static net.minecraftforge.client.GuiIngameForge.right_height;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Energy.MODID, value = Side.CLIENT)
public class HungerRender {
    private static final Random rand = new Random();
    private static EnergyFoodStats stats;

    public static int getMaxFoodForDisplay(Minecraft mc) {
        stats = mc.player.getFoodStats() instanceof EnergyFoodStats ? (EnergyFoodStats) mc.player.getFoodStats() : stats;
        return stats != null ? stats.maxFoodDisplay / 2 : 10;
    }

    public static int getMaxFood(Minecraft mc) {
        stats = mc.player.getFoodStats() instanceof EnergyFoodStats ? (EnergyFoodStats) mc.player.getFoodStats() : stats;
        return stats != null ? stats.maxFoodDisplay : 20;
    }

    @SuppressWarnings("unused")
    //@SubscribeEvent
    public static void onHungerRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.FOOD) {
            Minecraft mc = Minecraft.getMinecraft();
            int width = event.getResolution().getScaledWidth();
            int height = event.getResolution().getScaledHeight();
            mc.profiler.startSection("food");
            stats = mc.player.getFoodStats() instanceof EnergyFoodStats ? (EnergyFoodStats) mc.player.getFoodStats() : stats;
            if (stats != null) {
                EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
                GlStateManager.enableBlend();
                int left = width / 2 + 91;
                int top = height - right_height;
                //right_height += 10;

                int level = stats.getFoodLevel();
                for (int i = 0; i < stats.maxFoodDisplay / 2; ++i) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;
                    int y = top;
                    int icon = 16;
                    byte background = 0;

                    if (mc.player.isPotionActive(MobEffects.HUNGER)) {
                        icon += 36;
                        background = 13;
                    }

                    rand.setSeed(mc.ingameGUI.getUpdateCounter() * 312871L);
                    if (stats.getSaturationLevel() <= 0.0F && mc.ingameGUI.getUpdateCounter() % (level * 3 + 1) == 0) {
                        y = top + (rand.nextInt(3) - 1);
                    }

                    mc.ingameGUI.drawTexturedModalRect(x, y, 16 + background * 9, 27, 9, 9);

                    if (idx < level)
                        mc.ingameGUI.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
                    else if (idx == level)
                        mc.ingameGUI.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
                }

                GlStateManager.disableBlend();
                mc.profiler.endSection();
                MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(new RenderGameOverlayEvent(event.getPartialTicks(), event.getResolution()), ElementType.FOOD));
            }
        }
    }
}
