package joshie.harvest.core.commands;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@HFEvents
public class CommandManager extends CommandTreeBase {
    public static final CommandManager INSTANCE = new CommandManager();

    @Override
    @Nonnull
    public String getName() {
        return HFModInfo.COMMANDNAME;
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
            } catch (Exception ignored) {
            }
        }
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
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public enum CommandLevel {
        ANYONE, OP_BYPASS_PROTECTION, OP_AFFECT_GAMEPLAY, OP_BAN_PLAYERS, OP_STOP_SERVER
    }
}