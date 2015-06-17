package joshie.harvest.core.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.CommandEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class CommandManager extends CommandBase implements ICommand {
    public static final CommandManager INSTANCE = new CommandManager();
    private HashMap<String, HFCommandBase> commands = new HashMap();

    public void registerCommand(HFCommandBase command) {
        commands.put(command.getCommandName(), command);
    }

    public HFCommandBase getCommandFromString(String name) {
        return commands.get(name);
    }

    public Map getCommands() {
        return commands;
    }

    public List getPossibleCommands(ICommandSender sender) {
        ArrayList list = new ArrayList();
        for (HFCommandBase command : commands.values()) {
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
            return DimensionManager.getWorld(0);
        } else return MCClientHelper.getWorld();
    }

    @SubscribeEvent
    public void onCommandSend(CommandEvent event) {
        if (event.command == this && event.parameters.length > 0) {
            if (getWorld(event.sender).isRemote) event.setCanceled(true);
            else {
                String commandName = event.parameters[0];
                HFCommandBase command = commands.get(commandName);
                if (command == null || !event.sender.canCommandSenderUseCommand(command.getPermissionLevel().ordinal(), commandName)) {
                    event.setCanceled(true);
                } else {
                    processCommand(event, command);
                }
            }
        }
    }

    //Attempt to process the command, throw wrong usage otherwise
    private void processCommand(CommandEvent event, HFCommandBase command) {
        String[] args = new String[event.parameters.length - 1];
        System.arraycopy(event.parameters, 1, args, 0, args.length);
        if (!command.processCommand(event.sender, args)) {
            throwError(event.sender, command);
        }
    }

    static void throwError(ICommandSender sender, HFCommandBase command) {
        ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation(getUsage(command), new Object[0]);
        chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.RED);
        sender.addChatMessage(chatcomponenttranslation1);
    }

    static String getUsage(HFCommandBase command) {
        return "/" + INSTANCE.getCommandName() + " " + command.getCommandName() + " " + command.getUsage();
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return new ArrayList();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] values) {
        if (values.length == 0) {
            throwError(sender, new HFCommandHelp());
        }
    } //Do sweet nothing

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
