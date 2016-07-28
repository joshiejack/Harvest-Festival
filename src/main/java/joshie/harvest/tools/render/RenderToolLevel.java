package joshie.harvest.tools.render;

import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;
import java.util.List;

public class RenderToolLevel {
    private static final Field THE_SLOT = ReflectionHelper.findField(GuiContainer.class, ObfuscationReflectionHelper.remapFieldNames(GuiContainer.class.getName(), "theSlot", "field_147006_u", "u"));

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != Phase.END) return;
        Minecraft mc = MCClientHelper.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GuiContainer && mc.thePlayer.inventory.getItemStack() == null) {
            try {
                Slot slot = (Slot) THE_SLOT.get(gui);
                //Mouse is hovering over this slot
                ItemStack stack = slot.getStack();
                boolean isLevelable = stack.getItem() instanceof ILevelable;
                if (isLevelable) {
                    final ScaledResolution scaledresolution = ((GuiIngameForge) mc.ingameGUI).getResolution();
                    int i = scaledresolution.getScaledWidth();
                    int j = scaledresolution.getScaledHeight();
                    final int k = Mouse.getX() * i / mc.displayWidth;
                    final int l = j - Mouse.getY() * j / mc.displayHeight - 1;
                    List<String> tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
                    ToolTier tier = ToolTier.BASIC;
                    int level = ((ILevelable) stack.getItem()).getLevel(stack);
                    if (stack.getItem() instanceof ITiered) {
                        tier = ((ITiered) stack.getItem()).getTier(stack);
                    }

                    int y = tooltip.size() == 0 ? 0 : 2 + (10 * tooltip.size());
                    drawToolProgress(gui, k, l + 5 + y, tier, level, mc.fontRendererObj);
                }
            } catch (Exception e) {}
        }
    }

    @SideOnly(Side.CLIENT)
    private void drawToolProgress(GuiScreen gui, int x, int y, ToolTier tier, int level, FontRenderer font) {
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        int k = level < 10 ? 65 : level < 100 ? 70 : 80;
        int i1 = x + 12;
        int j1 = y - 12;
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
        gui.mc.renderEngine.bindTexture(HFModInfo.stars);
        gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, 8, 52, 9);
        int posY = tier != null && tier.ordinal() >= ToolTier.MYSTRIL.ordinal() ? 26 : 17;
        int width = (level >> 1) + 2;
        gui.drawTexturedModalRect(i1 - 1, j1 - 1, 0, posY, width, 9);

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.translate(53F, 0F, 0F);
        gui.mc.fontRendererObj.drawString(level + "%", i1, j1, 0xFFFFFF);
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}