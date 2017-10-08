package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandFestival extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "festival";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf festival <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1 && sender instanceof Entity) {
            try {
                Festival newFestival = parameters[0].contains(":") ? Festival.REGISTRY.get(new ResourceLocation(parameters[0])) : Festival.REGISTRY.get(new ResourceLocation(MODID, parameters[0]));
                TownDataServer town = TownHelper.getClosestTownToEntity(((Entity) sender), false);
                town.startFestival(newFestival); //Update the building right away
                long i = sender.getEntityWorld().getWorldTime() + TICKS_PER_DAY;
                CalendarHelper.setWorldTime(server, (i - i % TICKS_PER_DAY) - 1);
            } catch (NumberFormatException ignored) {}
        } else throw new WrongUsageException(getUsage(sender));
    }
}