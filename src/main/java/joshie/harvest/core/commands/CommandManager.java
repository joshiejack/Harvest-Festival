package joshie.harvest.core.commands;

import joshie.harvest.api.HFCommand;
import joshie.harvest.calendar.CalendarServer;
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
    private HashMap<String, HFCommand> commands = new HashMap<>();

    public void registerCommand(HFCommand command) {
        commands.put(command.getCommandName(), command);
    }

    public Map<String, HFCommand> getCommands() {
        return commands;
    }

    public List<HFCommand> getPossibleCommands(ICommandSender sender) {
        ArrayList<HFCommand> list = new ArrayList<>();
        for (HFCommand command : commands.values()) {
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

    @SubscribeEvent
    public void onCommandSend(CommandEvent event) throws CommandNotFoundException, NumberInvalidException {
        //Update the calendar
        if (event.getCommand().getCommandName().equals("time")) {
            try {
                event.getCommand().execute(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), event.getParameters());
                World world = event.getSender().getEntityWorld();
                HFTrackers.<CalendarServer>getCalendar(world).recalculateAndUpdate(world);
                event.setCanceled(true);
            } catch (Exception e) {}
        } else {
            //Otherwise process the command
            if (event.getCommand() == this && event.getParameters().length > 0) {
                if (getWorld(event.getSender()).isRemote) event.setCanceled(true);
                else {
                    String commandName = event.getParameters()[0];
                    HFCommand command = commands.get(commandName);
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
    private void processCommand(CommandEvent event, HFCommand command) throws CommandNotFoundException, NumberInvalidException {
        String[] args = new String[event.getParameters().length - 1];
        System.arraycopy(event.getParameters(), 1, args, 0, args.length);
        if (!command.execute(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), args)) {
            throwError(event.getSender(), command);
        }
    }

    static void throwError(ICommandSender sender, HFCommand command) {
        TextComponentTranslation textComponents = new TextComponentTranslation(getUsage(command), 0);
        textComponents.getStyle().setColor(TextFormatting.RED);
        sender.addChatMessage(textComponents);
    }

    static String getUsage(HFCommand command) {
        return "/" + INSTANCE.getCommandName() + " " + command.getCommandName() + " " + command.getUsage();
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