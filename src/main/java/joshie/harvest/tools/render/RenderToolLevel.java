package joshie.harvest.tools.render;

import joshie.harvest.api.core.ITiered;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@HFEvents(Side.CLIENT)
public class RenderToolLevel {
    @SubscribeEvent
    public void onTooltipRender(RenderTooltipEvent.PostText event) {
        if (event.getStack() == null || Minecraft.getMinecraft().currentScreen == null) return; //Do nothing if stack is null
        ItemStack stack = event.getStack();
        if (stack.getItem() instanceof ITiered) {
            ITiered tiered = ((ITiered)stack.getItem());
            double level = tiered.getLevel(stack);
            ToolTier tier = tiered.getTier(stack);
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            int k = level < 10 ? 65 : level < 100 ? 70 : 80;
            int i1 = event.getX();
            int j1 = event.getY() + 18 + (11 * (event.getLines().size() - 1));
            int k1 = 7;

            if (i1 + k > gui.width) {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > gui.height) {
                j1 = gui.height - k1 - 6;
            }

            int zLevel = (int) 500.0F;
            int l1 = -267386864;
            GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            GuiUtils.drawGradientRect(zLevel, i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            GuiUtils.drawGradientRect(zLevel, i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            GuiUtils.drawGradientRect(zLevel, i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            GuiUtils.drawGradientRect(zLevel, i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            gui.mc.renderEngine.bindTexture(HFModInfo.STARS);
            gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, 8, 52, 9);
            int posY = tier != null && tier.ordinal() >= ToolTier.MYSTRIL.ordinal() ? 26 : 17;
            int width = ((int)level >> 1) + 2;
            gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, posY, width, 9);

            GlStateManager.pushMatrix();
            GlStateManager.disableBlend();
            GlStateManager.translate(53F, 0F, 0F);
            gui.mc.fontRendererObj.drawString((int)level + "%", i1, j1, 0xFFFFFF);
            GlStateManager.enableBlend();
            GlStateManager.popMatrix();

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}