package joshie.harvest.plugins.morpheus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.api.INewDayHandler;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class SleepHandlerOverworld implements INewDayHandler {
    @Override
    public void startNewDay()  {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        long i = server.worldServerForDimension(0).getWorldTime() + TICKS_PER_DAY;
        for (int j = 0; j < server.worlds.length; ++j) {
            WorldServer world = server.worlds[j];
            world.setWorldTime((i - i % TICKS_PER_DAY) - 1);
            world.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).forEach(entityplayer -> entityplayer.wakeUpPlayer(false, false, true));
        }
    }
}
