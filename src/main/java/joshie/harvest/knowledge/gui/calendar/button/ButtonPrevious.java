package joshie.harvest.knowledge.gui.calendar.button;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.knowledge.gui.calendar.GuiCalendar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

import static joshie.harvest.knowledge.gui.calendar.GuiCalendar.CALENDAR_TEXTURE;

public class ButtonPrevious extends GuiButton {
    private final GuiCalendar gui;

    public ButtonPrevious(GuiCalendar calendar, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        gui = calendar;
        width = 12;
        height = 14;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            mc.getTextureManager().bindTexture(CALENDAR_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            int state = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, 220 + state * 12, 39, width, height);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        Season season = GuiCalendar.season;
        if (season == Season.SPRING) {
            if (GuiCalendar.year != 0) {
                GuiCalendar.season = Season.WINTER;
                GuiCalendar.year--;
            }
        } else GuiCalendar.season = CalendarHelper.SEASONS[season.ordinal() - 1];

        //Init the gui to reload the buttons
        gui.initGui(); ///RELOOOOOOAD????????????????????????!!!!!!!!!!!!??
    }
}
