package joshie.harvest.knowledge.gui.calendar.button;

import joshie.harvest.api.calendar.CalendarEntry;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.knowledge.gui.calendar.GuiCalendar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;

public class ButtonDate extends GuiButton {
    private final GuiCalendar gui;
    private final CyclingStack icons;

    public ButtonDate(GuiCalendar gui, int number, List<CalendarEntry> entries, int x, int y) {
        super(number, x, y, "");
        this.gui = gui;
        this.icons = new CyclingStack(x + 8, y + 6, entries);
        this.width = 26;
        this.height = 26;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            //If it's the same date, draw the highlight
            mc.getTextureManager().bindTexture(GuiCalendar.CALENDAR_TEXTURE);
            if (id == GuiCalendar.date.getDay() && GuiCalendar.season == GuiCalendar.date.getSeason() && GuiCalendar.year == GuiCalendar.date.getYear()) {
                drawTexturedModalRect(xPosition - 2, yPosition - 2, 10, 54, 26, 26);
            }

            boolean prev = mc.fontRendererObj.getUnicodeFlag();
            mc.fontRendererObj.setUnicodeFlag(true);
            gui.drawString(mc.fontRendererObj, TextFormatting.BOLD + "" + (id + 1), xPosition + 2, yPosition, 0xFFFFFF);

            mc.fontRendererObj.setUnicodeFlag(prev);
            mouseDragged(mc, mouseX, mouseY);
            icons.render(gui, mouseX, mouseY);
            if (DAYS_PER_SEASON != 30 && hovered) {
                int modifier = DAYS_PER_SEASON / 30;
                int min = (id * modifier) + 1;
                int max = (id * modifier) + modifier;
                gui.addTooltip(min + "-" + max);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) { }

    private static class CyclingStack {
        private final int x;
        private final int y;
        private final List<CalendarEntry> entries;
        private List<String> tooltip;
        private ItemStack stack;
        private int ticker;
        private int index;

        @SuppressWarnings("unchecked")
        CyclingStack(int x, int y, List<CalendarEntry> entries) {
            this.x = x;
            this.y = y;
            this.entries = entries;
        }

        public void render(GuiCalendar gui, int mouseX, int mouseY) {
            if (entries.size() > 0) {
                if (ticker % 128 == 0 || stack == null) {
                    stack = entries.get(index).getStackRepresentation(); //Pick out the stack
                    tooltip = new ArrayList<>();
                    entries.get(index).addTooltipForCalendarEntry(tooltip);
                    index++;
                    if (index >= entries.size()) {
                        index = 0; //Reset the index
                    }
                }

                StackRenderHelper.drawStack(stack, x, y, 1F);
                if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY < y + 16) {
                    gui.addTooltip(tooltip);
                    if (DAYS_PER_SEASON != 30) gui.addTooltip("------");
                } else ticker++;

                GlStateManager.enableDepth();
            }
        }
    }
}
