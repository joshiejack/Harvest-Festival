package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.IGreeting;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class GreetingTime implements IGreeting {
    @Override
    @SuppressWarnings("deprecation")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc) {
        int years = CalendarHelper.getYearsPassed(HFTrackers.getClientPlayerTracker().getStats().getBirthday(), HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate());
        return I18n.translateToLocalFormatted("harvestfestival.npc.tiberius.time", years);
    }
}
