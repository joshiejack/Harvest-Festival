package joshie.harvest.fishing.render;

import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
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
@SuppressWarnings("unused")
public class RenderBait {
    @SubscribeEvent
    public void onTooltipRender(RenderTooltipEvent.PostText event) {
        if (event.getStack() == null || Minecraft.getMinecraft().currentScreen == null) return; //Do nothing if stack is null
        ItemStack stack = event.getStack();
        if (stack.getItem() == HFFishing.FISHING_ROD) {
            int amount = HFFishing.FISHING_ROD.getBaitAmount(stack);
            if (amount > 0) {
                GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();

                int k = 8;
                int i1 = event.getX() + 75;
                int j1 = event.getY() + 18 + (11 * (event.getLines().size() - 1));
                int k1 = 7;

                if (i1 + k > gui.width) {
                    //i1 -= 28 + k;
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
                int posY = 17;

                ItemStack bait = amount > 1 ? HFFishing.JUNK.getStackFromEnum(Junk.BAIT, amount) : HFFishing.JUNK.getStackFromEnum(Junk.BAIT, 1);
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                StackRenderHelper.drawStack(bait, i1, j1, 0.5F);
                GlStateManager.enableBlend();
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableRescaleNormal();
            }
        }
    }
}