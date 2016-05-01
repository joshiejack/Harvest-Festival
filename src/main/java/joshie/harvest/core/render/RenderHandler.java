package joshie.harvest.core.render;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ILevelable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.List;

public class RenderHandler {
    //Orange Leaves in Autumn
    @SubscribeEvent
    public void getFoliageColor(GetFoliageColor event) {
        if (HFTrackers.getCalendar().getDate().getSeason() == Season.AUTUMN) {
            event.setNewColor(0xFF9900);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = MCClientHelper.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GuiContainer && mc.thePlayer.inventory.getItemStack() == null) {
            GuiContainer container = (GuiContainer) gui;
            Point mouse = MCClientHelper.getMouse(container);
            final ScaledResolution scaledresolution = ((GuiIngameForge) mc.ingameGUI).getResolution();
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            final int k = Mouse.getX() * i / mc.displayWidth;
            final int l = j - Mouse.getY() * j / mc.displayHeight - 1;

            List<Slot> slots = mc.thePlayer.openContainer.inventorySlots;
            for (Slot slot : slots) {
                if (mouse.x >= slot.xDisplayPosition - 1 && mouse.x <= slot.xDisplayPosition + 16 && mouse.y >= slot.yDisplayPosition - 1 && mouse.y <= slot.yDisplayPosition + 16) {
                    //Mouse is hovering over this slot
                    ItemStack stack = slot.getStack();
                    if (stack == null) continue;
                    boolean isLevelable = stack.getItem() instanceof ILevelable;
                    if (isLevelable) {
                        List<String> tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
                        ToolTier tier = ToolTier.BASIC;
                        int level = ((ILevelable) stack.getItem()).getLevel(stack);
                        if (stack.getItem() instanceof ITiered) {
                            tier = ((ITiered) stack.getItem()).getTier(stack);
                        }

                        int y = tooltip.size() == 0 ? 0 : 2 + (10 * tooltip.size());
                        RenderToolLevel.drawToolProgress(gui, k, l + 5 + y, tier, level, mc.fontRendererObj);
                    }
                }
            }
        }
    }
}