package joshie.harvest.plugins.buttons;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.plugins.buttons.ButtonSeason.HFSeason;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tehnut.buttons.api.WidgetTexture;
import tehnut.buttons.api.button.utility.ButtonMode;
import tehnut.buttons.api.button.utility.IMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ButtonSeason extends ButtonMode<HFSeason> {
    public ButtonSeason() {
        super(Buttons.BLANK, HFSeason.class);
        setServerRequired(); //Check the server
    }

    @Override
    public ResourceLocation getButtonId() {
        return new ResourceLocation(MODID, "seasons");
    }

    public enum HFSeason implements IMode {
        SPRING(0, Season.SPRING),
        SUMMER(20, Season.SUMMER),
        AUTUMN(40, Season.AUTUMN),
        WINTER(60, Season.WINTER),;

        private final WidgetTexture widgetTexture;
        private final Season season;

        HFSeason(int x, Season season) {
            this.widgetTexture = new WidgetTexture(Buttons.RESOURCE, x, 20, 20, 20);
            this.season = season;
        }

        @Nonnull
        @Override
        public WidgetTexture getModeTexture() {
            return widgetTexture;
        }

        @Nullable
        @Override
        public List<? extends ITextComponent> getTooltip() {
            return Collections.singletonList(new TextComponentTranslation("harvestfestival.season." + season.name().toLowerCase(Locale.ENGLISH)));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public EnumActionResult onClientClick(int mouseX, int mouseY) {
            return EnumActionResult.SUCCESS;
        }

        @Override
        public void onServerClick(EntityPlayerMP player) {
            World world = player.worldObj;
            CalendarServer calendar = HFTrackers.getCalendar(world);
            int day = calendar.getDate().getDay();
            int year = Math.max(1, calendar.getDate().getYear());
            long leftover = player.worldObj.getWorldTime() % HFCalendar.TICKS_PER_DAY;
            world.setWorldTime(CalendarHelper.getTime(day, season, year) + leftover);
            calendar.recalculateAndUpdate(world);
        }
    }
}
