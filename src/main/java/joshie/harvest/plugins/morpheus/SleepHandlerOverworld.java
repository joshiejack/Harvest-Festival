package joshie.harvest.plugins.morpheus;

import joshie.harvest.tools.HFTools;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.api.INewDayHandler;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class SleepHandlerOverworld implements INewDayHandler {
    @Override
    public void startNewDay() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        long i = server.worldServerForDimension(0).getWorldTime() + TICKS_PER_DAY;
        for (int j = 0; j < server.worlds.length; ++j) {
            WorldServer world = server.worlds[j];
            world.setWorldTime((i - i % TICKS_PER_DAY) - 1);
            if (HFTools.RESTORE_HUNGER_ON_SLEEP) {
                if (HFTools.RESTORE_HUNGER_FOR_SLEEPERS_ONLY) {
                    world.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).forEach(ToolHelper::restoreHunger);
                } else {
                    world.playerEntities.forEach(ToolHelper::restoreHunger);
                }
            }
            world.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).forEach(player -> player.wakeUpPlayer(false, false, true));
        }
    }
}