package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.CalendarHelper.getClientDay;
import static joshie.harvestmoon.helpers.CalendarHelper.getClientSeason;
import static joshie.harvestmoon.helpers.CalendarHelper.getClientYear;

import java.awt.Point;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import joshie.harvestmoon.gui.ContainerNPC;
import joshie.harvestmoon.helpers.RatingHelper;
import joshie.harvestmoon.util.IRateable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderEvents {    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GuiContainer && mc.thePlayer.inventory.getItemStack() == null) {
            GuiContainer container = (GuiContainer) gui;
            Point mouse = joshie.lib.helpers.ClientHelper.getMouse(container);

            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glDisable(2896);
            List slots = mc.thePlayer.openContainer.inventorySlots;
            for (int o = 0; o < slots.size(); o++) {
                Slot slot = (Slot) slots.get(o);
                //Mouse is hovering over this slot
                ItemStack stack = slot.getStack();
                if (stack != null && stack.getItem() instanceof IRateable) {
                    if (mouse.x >= slot.xDisplayPosition - 1 && mouse.x <= slot.xDisplayPosition + 16 && mouse.y >= slot.yDisplayPosition - 1 && mouse.y <= slot.yDisplayPosition + 16) {
                        final ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                        int i = scaledresolution.getScaledWidth();
                        int j = scaledresolution.getScaledHeight();
                        final int k = Mouse.getX() * i / mc.displayWidth;
                        final int l = j - Mouse.getY() * j / mc.displayHeight - 1;
                        ArrayList<String> tooltip = new ArrayList();
                        stack.getItem().addInformation(stack, mc.thePlayer, tooltip, false);
                        int rating = ((IRateable) stack.getItem()).getRating(stack);
                        RatingHelper.drawStarRating(gui, k, l + 17 + (12 * tooltip.size()), rating, mc.fontRenderer);
                    }
                }
            }

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if(event.type == ElementType.CROSSHAIRS) {
            if(joshie.lib.helpers.ClientHelper.getPlayer().openContainer instanceof ContainerNPC) {
                event.setCanceled(true);
            }
        } else if (event.type == ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.ingameGUI.remainingHighlightTicks = 0;
            mc.mcProfiler.startSection("calendarHUD");

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(getClientSeason().getTexture());

            int x = (int) (event.resolution.getScaledWidth() * 3.35 + 10);
            int y = (int) event.resolution.getScaledHeight() - event.resolution.getScaledHeight() - 40;
            mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

            GL11.glPushMatrix();
            GL11.glScalef(1.8F, 1.8F, 1.8F);
            mc.fontRenderer.drawString(getClientSeason().getLocalized() + " " + getClientDay(), 5, 4, 0);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            mc.fontRenderer.drawString("Year " + getClientYear(), 8, 15, 0);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glScalef(1.2F, 1.2F, 1.2F);
            mc.fontRenderer.drawString(NumberFormat.getNumberInstance(Locale.US).format(handler.getClient().getPlayerData().getGold()), 9, 36, 0);
            GL11.glPopMatrix();

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            mc.mcProfiler.endSection();
        }
    }
}
