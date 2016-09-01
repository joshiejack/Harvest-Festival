package joshie.harvest.core.commands;

import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@HFEvents
public class CommandManager extends CommandBase implements ICommand {
    public static final CommandManager INSTANCE = new CommandManager();
    private HashMap<String, AbstractHFCommand> commands = new HashMap<>();

    public void registerCommand(AbstractHFCommand command) {
        commands.put(command.getCommandName(), command);
    }

    public Map<String, AbstractHFCommand> getCommands() {
        return commands;
    }

    public List<AbstractHFCommand> getPossibleCommands(ICommandSender sender) {
        ArrayList<AbstractHFCommand> list = new ArrayList<>();
        for (AbstractHFCommand command : commands.values()) {
            if (sender.canCommandSenderUseCommand(command.getPermissionLevel().ordinal(), command.getCommandName())) {
                list.add(command);
            }
        }

        return list;
    }

    @Override
    public String getCommandName() {
        return HFModInfo.COMMANDNAME;
    }

    private World getWorld(ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            return ((EntityPlayer) sender).worldObj;
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        } else return MCClientHelper.getWorld();
    }



    private boolean executeVanillaTime(MinecraftServer server, ICommandSender sender, String[] parameters) throws NumberInvalidException {
        if (parameters.length > 1) {
            if (parameters[0].equals("set")) {
                long time = CalendarHelper.getElapsedDays(server.worldServers[0].getWorldTime()) * HFCalendar.TICKS_PER_DAY;
                if (parameters[1].equals("force-day")) {
                    time = 1000;
                } else if (parameters[1].equals("force-night")) {
                    time = 13000;
                } else if (parameters[1].equals("force")) {
                    time = parseInt(parameters[1]);
                } else if (parameters[1].equals("day")) {
                    time += 3000;
                } else if (parameters[1].equals("night")) {
                    time += 18000;
                } else {
                    time += parseInt(parameters[1]);
                }

                for (int i = 0; i < server.worldServers.length; ++i) {
                    WorldServer worldserver = server.worldServers[i];
                    worldserver.setWorldTime(time);
                }

                //TODO: Make set time also add time
                HFTrackers.<CalendarServer>getCalendar(server.worldServers[0]).recalculateAndUpdate(server.worldServers[0]);
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onCommandSend(CommandEvent event) throws CommandNotFoundException, NumberInvalidException {
        //Update the calendar
        if (event.getCommand().getCommandName().equals("time")) {
            try {
                if (executeVanillaTime(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), event.getParameters())) {
                    event.setCanceled(true);
                }
            } catch (Exception e) {}
        } else {
            //Otherwise process the command
            if (event.getCommand() == this && event.getParameters().length > 0) {
                if (getWorld(event.getSender()).isRemote) event.setCanceled(true);
                else {
                    String commandName = event.getParameters()[0];
                    AbstractHFCommand command = commands.get(commandName);
                    if (command == null || !event.getSender().canCommandSenderUseCommand(command.getPermissionLevel().ordinal(), commandName)) {
                        event.setCanceled(true);
                    } else {
                        processCommand(event, command);
                    }
                }
            }
        }
    }

    //Attempt to process the command, throw wrong usage otherwise
    private void processCommand(CommandEvent event, AbstractHFCommand command) throws CommandNotFoundException, NumberInvalidException {
        String[] args = new String[event.getParameters().length - 1];
        System.arraycopy(event.getParameters(), 1, args, 0, args.length);
        if (!command.execute(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), args)) {
            throwError(event.getSender(), command);
        }
    }

    static void throwError(ICommandSender sender, AbstractHFCommand command) {
        TextComponentTranslation textComponents = new TextComponentTranslation(getUsage(command), 0);
        textComponents.getStyle().setColor(TextFormatting.RED);
        sender.addChatMessage(textComponents);
    }

    static String getUsage(AbstractHFCommand command) {
        return command.getUsage();
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] parameters, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] values) {
        if (values.length == 0) {
            throwError(sender, new HFCommandHelp());
        }
    } //Do sweet nothing

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}