package uk.joshiejack.economy.client;

import uk.joshiejack.economy.client.gui.GuiShop;
import uk.joshiejack.economy.gold.Wallet;
import uk.joshiejack.economy.EconomyConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.text.NumberFormat;
import java.util.Locale;

import static uk.joshiejack.economy.Economy.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class RenderGold {
    @SubscribeEvent
    public static void offsetPotion(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, 16F, 0F);
        }
    }

    @SubscribeEvent
    public static void offsetPotion(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int maxWidth = event.getResolution().getScaledWidth();
            int maxHeight = event.getResolution().getScaledHeight();
            if (EconomyConfig.enableHUD) {
                String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(Wallet.getActive().getBalance());
                float adjustedX = ((EconomyConfig.goldX / 100F) * maxWidth);
                float adjustedY = ((EconomyConfig.goldY / 100F) * maxHeight);

                if (EconomyConfig.enableGoldIcon) {
                    mc.getTextureManager().bindTexture(GuiShop.EXTRA);
                    int coinX = (int) (EconomyConfig.goldLeft ? maxWidth - mc.fontRenderer.getStringWidth(text) - 20 + adjustedX : maxWidth - adjustedX - 14);
                    mc.ingameGUI.drawTexturedModalRect(coinX, 2 + adjustedY, 244, 244, 12, 12);
                }

                int textX = (int)(EconomyConfig.goldLeft ? maxWidth - mc.fontRenderer.getStringWidth(text) - 5 + (int) adjustedX : maxWidth - adjustedX - 18 - mc.fontRenderer.getStringWidth(text));
                mc.fontRenderer.drawStringWithShadow(text, textX, 4 + adjustedY, 0xFFFFFFFF);
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
