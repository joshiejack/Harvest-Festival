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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderEvents {    
    private static final ResourceLocation COIN_TEXTURE = new ResourceLocation("hm", "textures/gui/gui_elements.png");
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;
        if (gui instanceof GuiContainer && mc.thePlayer.inventory.getItemStack() == null) {
            GuiContainer container = (GuiContainer) gui;
            Point mouse = joshie.harvestmoon.helpers.generic.ClientHelper.getMouse(container);

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
            if(joshie.harvestmoon.helpers.generic.ClientHelper.getPlayer().openContainer instanceof ContainerNPC) {
                event.setCanceled(true);
            }
        } else if (event.type == ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.ingameGUI.remainingHighlightTicks = 0;
            mc.mcProfiler.startSection("calendarHUD");

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(getClientSeason().getTexture());
            mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

            //Enlarge the Day
            GL11.glPushMatrix();
            GL11.glScalef(1.4F, 1.4F, 1.4F);
            mc.fontRenderer.drawStringWithShadow(getClientSeason().getLocalized() + " " + getClientDay(), 30, 8, 0xFFFFFFFF);
            GL11.glPopMatrix();

            mc.fontRenderer.drawStringWithShadow("Year " + getClientYear(), 45, 25, 0xFFFFFFFF);
            mc.getTextureManager().bindTexture(COIN_TEXTURE);
            String text = NumberFormat.getNumberInstance(Locale.US).format(handler.getClient().getPlayerData().getGold());
            int width = event.resolution.getScaledWidth();
            mc.ingameGUI.drawTexturedModalRect(width - mc.fontRenderer.getStringWidth(text) - 20, 2, 244, 0, 12, 12);
            mc.fontRenderer.drawStringWithShadow(text, width - mc.fontRenderer.getStringWidth(text) - 5, 5, 0xFFFFFFFF);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            mc.mcProfiler.endSection();
        }
    }
}
