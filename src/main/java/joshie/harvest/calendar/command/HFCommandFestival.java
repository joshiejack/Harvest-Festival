package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandFestival extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "festival";
    }

    @Override
    public String getUsage() {
        return "/hf festival <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1 && sender instanceof Entity) {
            try {
                Festival newFestival = parameters[0].contains(":") ? Festival.REGISTRY.get(new ResourceLocation(parameters[0])) : Festival.REGISTRY.get(new ResourceLocation(MODID, parameters[0]));
                TownDataServer town = TownHelper.getClosestTownToEntity(((Entity) sender), false);
                town.startFestival(newFestival); //Update the building right away
                long i = sender.getEntityWorld().getWorldTime() + TICKS_PER_DAY;
                CalendarHelper.setWorldTime(server, (i - i % TICKS_PER_DAY) - 1);
                return true;
            } catch (NumberFormatException ignored) {}
        }
        return false;
    }
}