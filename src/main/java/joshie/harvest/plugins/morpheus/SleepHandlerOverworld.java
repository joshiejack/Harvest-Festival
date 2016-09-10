package joshie.harvest.plugins.morpheus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.api.INewDayHandler;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class SleepHandlerOverworld implements INewDayHandler {
    @Override
    public void startNewDay()  {
        WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
        long i = world.getWorldTime() + TICKS_PER_DAY;
        world.setWorldTime((i - i % TICKS_PER_DAY) - 1);
        for (EntityPlayer entityplayer : world.playerEntities) {
            if (entityplayer.isPlayerSleeping())  {
                entityplayer.wakeUpPlayer(false, false, true);
            }
        }
    }
}
