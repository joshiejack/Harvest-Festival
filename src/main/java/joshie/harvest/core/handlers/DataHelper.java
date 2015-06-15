package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.helpers.ClientHelper;
import joshie.harvest.core.helpers.ServerHelper;
import joshie.harvest.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class DataHelper {
    private static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }

    public static AnimalTracker getAnimalTracker() {
        return isServer() ? ServerHelper.getAnimalTracker() : ClientHelper.getAnimalTracker();
    }

    public static PlayerTracker getPlayerTracker(EntityPlayer player) {
        return isServer() ? ServerHelper.getPlayerData(player) : ClientHelper.getPlayerData();
    }

    public static Calendar getCalendar() {
        return isServer() ? ServerHelper.getCalendar() : ClientHelper.getCalendar();
    }
}
