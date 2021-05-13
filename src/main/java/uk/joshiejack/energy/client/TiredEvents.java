package uk.joshiejack.energy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyEffects;

import java.awt.*;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Energy.MODID, value = Side.CLIENT)
public class TiredEvents {
    private static Color color = new Color(0, 0, 0, 0);
    private static int FADE_IN_OUT;
    private static boolean INCREASE = true;
    private static boolean BLINK = false;

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void renderTiredness(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            if (!player.isCreative() && player.isPotionActive(EnergyEffects.TIRED)) {
                if (player.world.getWorldTime() %200 == 0) BLINK = true;

                if (BLINK) {
                    if (INCREASE) {
                        FADE_IN_OUT++;
                    } else FADE_IN_OUT--;


                    color = new Color(0, 0, 0, Math.min(255, FADE_IN_OUT / 2));
                    if (FADE_IN_OUT > (255 * 2.5) || FADE_IN_OUT < 0) {
                        INCREASE = !INCREASE;
                        if (FADE_IN_OUT <= 0) {
                            BLINK = false;
                        }
                    }

                    GlStateManager.pushMatrix();
                    ScaledResolution sr = new ScaledResolution(mc);
                    GlStateManager.depthMask(false);
                    Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), color.getRGB());
                    GlStateManager.depthMask(true);
                    mc.getTextureManager().bindTexture(Gui.ICONS); //rebind the old texture
                    //GlStateManager.enableBlend();
                    //GlStateManager.enableTexture2D();
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
