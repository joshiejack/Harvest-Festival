package joshie.harvest.npc.greeting;

import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import net.minecraft.util.text.translation.I18n;

public class GreetingTime extends GreetingSingle {
    public GreetingTime(String text) {
        super(text);
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getLocalizedText(String text) {
        int years = CalendarHelper.getYearsPassed(HFTrackers.getClientPlayerTracker().getStats().getBirthday(), HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate());
        return I18n.translateToLocalFormatted(text, years);
    }
}
