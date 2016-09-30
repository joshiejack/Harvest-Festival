package joshie.harvest.npc.greeting;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class GreetingWeather extends GreetingSingle {
    public GreetingWeather(String text) {
        super(text);
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getLocalizedText(EntityPlayer player, EntityAgeable ageable, INPC npc, String text) {
        return getLocalized(HFTrackers.<CalendarClient>getCalendar(player.worldObj).getTomorrowsWeather().name().toLowerCase(Locale.ENGLISH) + (player.worldObj.rand.nextInt(3) + 1));
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public String getLocalized(String text) {
        return I18n.translateToLocal(getUnlocalizedText(null, null, null) + "." + text);
    }
}
