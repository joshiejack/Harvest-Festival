package joshie.harvest.core.handlers.events;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.helpers.ToolLevelHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.items.ItemBaseTool.ToolTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ToolLevelRender {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = MCClientHelper.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GuiContainer && mc.thePlayer.inventory.getItemStack() == null) {
            GuiContainer container = (GuiContainer) gui;
            Point mouse = joshie.harvest.core.helpers.generic.MCClientHelper.getMouse(container);
            final ScaledResolution scaledresolution = ((GuiIngameForge) mc.ingameGUI).getResolution();
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            final int k = Mouse.getX() * i / mc.displayWidth;
            final int l = j - Mouse.getY() * j / mc.displayHeight - 1;

            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glDisable(2896);
            List slots = mc.thePlayer.openContainer.inventorySlots;
            for (int o = 0; o < slots.size(); o++) {
                Slot slot = (Slot) slots.get(o);
                if (mouse.x >= slot.xDisplayPosition - 1 && mouse.x <= slot.xDisplayPosition + 16 && mouse.y >= slot.yDisplayPosition - 1 && mouse.y <= slot.yDisplayPosition + 16) {
                    //Mouse is hovering over this slot
                    ItemStack stack = slot.getStack();
                    if (stack == null) continue;
                    boolean isLevelable = stack.getItem() instanceof ILevelable;
                    if (isLevelable) {
                        ArrayList<String> tooltip = (ArrayList<String>) stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
                        ToolTier tier = ToolTier.BASIC;
                        int level = ((ILevelable) stack.getItem()).getLevel(stack);
                        if (stack.getItem() instanceof ITiered) {
                            tier = ((ITiered) stack.getItem()).getTier(stack);
                        }

                        int y = tooltip.size() == 0 ? 0 : 2 + (10 * tooltip.size());
                        ToolLevelHelper.drawToolProgress(gui, k, l + 5 + y, tier, level, mc.fontRenderer);
                    }
                }
            }

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
