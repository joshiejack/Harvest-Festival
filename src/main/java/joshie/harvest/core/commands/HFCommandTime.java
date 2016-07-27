package joshie.harvest.core.commands;

import joshie.harvest.api.HFRegister;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@HFRegister
public class HFCommandTime extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "time";
    }

    @Override
    public String getUsage() {
        return "set|add time";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters.length > 1) {
            if (parameters[0].equals("set")) {
                int i1;

                if (parameters[1].equals("day"))  {
                    i1 = 8000;
                } else if (parameters[1].equals("night")) {
                    i1 = 18000;
                } else {
                    i1 = parseInt(parameters[1]);
                }

                sender.getEntityWorld().setWorldTime(i1);
                HFTrackers.getCalendar(sender.getEntityWorld()).recalculateAndUpdate();
                return true;
            }

            if (parameters[0].equals("add")) {
                int l = parseInt(parameters[1]);
                sender.getEntityWorld().setWorldTime(sender.getEntityWorld().getWorldTime() + l);
                HFTrackers.getCalendar(sender.getEntityWorld()).recalculateAndUpdate();
                return true;
            }
        }

        return false;
    }
}