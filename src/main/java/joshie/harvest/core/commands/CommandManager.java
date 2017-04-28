package joshie.harvest.core.commands;

import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@HFEvents
public class CommandManager extends CommandBase {
    public static final CommandManager INSTANCE = new CommandManager();
    private final HashMap<String, AbstractHFCommand> commands = new HashMap<>();

    public void registerCommand(AbstractHFCommand command) {
        commands.put(command.getCommandName(), command);
    }

    public Map<String, AbstractHFCommand> getCommands() {
        return commands;
    }

    List<AbstractHFCommand> getPossibleCommands(ICommandSender sender) {
        return commands.values().stream().filter(command -> sender.canUseCommand(command.getPermissionLevel().ordinal(), command.getCommandName())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Nonnull
    public String getName() {
        return HFModInfo.COMMANDNAME;
    }

    private World getWorld(ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            return ((EntityPlayer) sender).world;
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        } else return MCClientHelper.getWorld();
    }


    @SubscribeEvent
    public void onCommandSend(CommandEvent event) throws CommandException {
        //Update the calendar
        if (VanillaCommands.isHandled(event.getCommand().getName())) {
            String name = event.getCommand().getName();
            try {
                if (name.equals("time") && VanillaCommands.executeVanillaTime(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), event.getParameters())) {
                    event.setCanceled(true);
                } else if (name.equals("weather") && VanillaCommands.executeVanillaWeather(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), event.getParameters())) {
                    event.setCanceled(true);
                } else if (name.equals("toggledownfall") && VanillaCommands.executeToggleDownfall(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), event.getParameters())) {
                    event.setCanceled(true);
                }
            } catch (Exception ignored) {}
        } else {
            //Otherwise process the command
            if (event.getCommand() == this && event.getParameters().length > 0) {
                if (getWorld(event.getSender()).isRemote) event.setCanceled(true);
                else {
                    String commandName = event.getParameters()[0];
                    AbstractHFCommand command = commands.get(commandName);
                    if (command == null || !event.getSender().canUseCommand(command.getPermissionLevel().ordinal(), commandName)) {
                        event.setCanceled(true);
                    } else {
                        processCommand(event, command);
                    }
                }
            }
        }
    }

    //Attempt to process the command, throw wrong usage otherwise
    private void processCommand(CommandEvent event, AbstractHFCommand command) throws CommandException {
        String[] args = new String[event.getParameters().length - 1];
        System.arraycopy(event.getParameters(), 1, args, 0, args.length);
        if (!command.execute(FMLCommonHandler.instance().getMinecraftServerInstance(), event.getSender(), args)) {
            throwError(event.getSender(), command);
        }
    }

    static void throwError(ICommandSender sender, AbstractHFCommand command) {
        TextComponentTranslation textComponents = new TextComponentTranslation(getUsage(command), 0);
        textComponents.getStyle().setColor(TextFormatting.RED);
        sender.sendMessage(textComponents);
    }

    static String getUsage(AbstractHFCommand command) {
        return command.getUsage();
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] parameters, @Nullable BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/" + getName() + " help";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] values) {
        if (values.length == 0) {
            throwError(sender, new HFCommandHelp());
        }
    } //Do sweet nothing

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}