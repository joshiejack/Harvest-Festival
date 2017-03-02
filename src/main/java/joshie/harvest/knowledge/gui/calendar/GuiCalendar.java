package joshie.harvest.knowledge.gui.calendar;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.CalendarEntry;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.base.gui.ContainerNull;
import joshie.harvest.core.base.gui.GuiBase;
import joshie.harvest.knowledge.gui.calendar.button.ButtonDate;
import joshie.harvest.knowledge.gui.calendar.button.ButtonNext;
import joshie.harvest.knowledge.gui.calendar.button.ButtonPrevious;
import joshie.harvest.shops.gui.ShopFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;
import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GuiCalendar extends GuiBase {
    public static final ResourceLocation CALENDAR_TEXTURE = new ResourceLocation(MODID, "textures/gui/calendar.png") ;
    private final Multimap<CalendarDate, CalendarEntry> entries = HashMultimap.create();
    public static CalendarDate date;
    public static Season season;
    public static int year = -1;
    private int rows;


    public GuiCalendar(EntityPlayer player) {
        super(new ContainerNull(), "calendar", 36);
        CalendarDate local = HFApi.calendar.getDate(player.worldObj);
        int day = (local.getDay() - 1) / (DAYS_PER_SEASON / 30);
        date = new CalendarDate(day, local.getSeason(), local.getYear());
        season = date.getSeason();
        year = date.getYear();
        xSize = 226;

        CalendarAPI.INSTANCE.getFestivals().entrySet().forEach((entry) -> {
            if (!entry.getValue().isHidden()) {
                entries.get(getNextDay(entry.getKey())).add(entry.getValue());
            }
        });

        NPC.REGISTRY.values().forEach(npc -> {
            if (npc != NPC.NULL_NPC && HFApi.player.getRelationsForPlayer(player).isStatusMet(npc, RelationStatus.MET)) {
                entries.get(npc.getBirthday()).add(npc);
            }
        });
    }

    private CalendarDate getNextDay(CalendarDate date) {
        if (date.getDay() < 30) return new CalendarDate(date.getDay() + 1, date.getSeason(), date.getYear());
        else {
            if (date.getSeason() == Season.WINTER) return new CalendarDate(1, Season.SPRING, date.getYear());
            else {
                Season next = CalendarHelper.SEASONS[season.ordinal() + 1];
                return new CalendarDate(1, next, date.getYear());
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        rows = getNumberOfRows();
        //int yExtra = rows
        for (int day = 0; day < 30; day++) {
            CalendarDate date = new CalendarDate(day, season, year);
            buttonList.add(new ButtonDate(this, day, getStacksForDate(new CalendarDate(day * (DAYS_PER_SEASON / 30), season, year)), guiLeft + getXForDate(date), guiTop + getYForDate(date)));
        }

        if ((GuiCalendar.year > 0 || (GuiCalendar.year == 0 && GuiCalendar.season != Season.SPRING))) {
            buttonList.add(new ButtonPrevious(this, buttonList.size(), guiLeft + 30, guiTop - 12));
        }

        buttonList.add(new ButtonNext(this, buttonList.size(), guiLeft + 180, guiTop - 12));
    }

    private List<CalendarEntry> getStacksForDate(CalendarDate date) {
        return entries.entries().stream().filter(entry -> entry.getKey().isSameDay(date)).map(Entry::getValue).collect(Collectors.toList());
    }

    private int getNumberOfRows() {
        int max = 0;
        for (int day = 0; day < 30; day++) {
            CalendarDate date = new CalendarDate(day, season, year);
            int season = (date.getYear() * 4) + date.getSeason().ordinal();
            int x = (((date.getDay() + (season * 2)) % 7));
            int value = (int) ((double) (date.getDay() - x) + 13) / 7;
            if (value > max) max = value;
        }

        return max;
    }

    private int getXForDate(CalendarDate date) {
        int season = (date.getYear() * 4) + date.getSeason().ordinal();
        int value = (((date.getDay() + (season * 2)) % 7));
        return 14 + (value * 30);
    }

    private int getYForDate(CalendarDate date) {
        int season = (date.getYear() * 4) + date.getSeason().ordinal();
        int x = (((date.getDay() + (season * 2)) % 7));
        int value = (int)((double)(date.getDay() - x) + 13) / 7;
        return (value * 30) - 6;
    }

    @Override
    protected void drawGuiTexture() {
        GlStateManager.color(1F, 1F, 1F);
        mc.renderEngine.bindTexture(CALENDAR_TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 22);
    }

    @Override
    public void drawBackground(int x, int y) {
        for (int i = 0; i < rows; i++) {
            if (i == 0) drawTexturedModalRect(guiLeft + 10, guiTop + 22, 8, 26, 210, 26);
            else drawTexturedModalRect(guiLeft + 10, guiTop + 18 + 30 * i, 8, 22, 210, 30);
        }

        drawTexturedModalRect(guiLeft + 10, guiTop + 18 + 30 * rows, 8, 52, 210, 2);
        drawCenteredString(fontRendererObj, season.getDisplayName() + TextFormatting.RESET + " - Year " + (year + 1), guiLeft + 113, guiTop - 10, 0xFFFFFF);
    }

    @Override
    public void drawForeground(int x, int y) {
        for (Weekday weekday: CalendarHelper.DAYS) {
            GlStateManager.color(1F, 1F, 1F);
            RenderHelper.disableStandardItemLighting();
            ShopFontRenderer.render(this, 12 + weekday.ordinal() * 30, 10, weekday.getLocalizedName(), 1F);
        }
    }
}
