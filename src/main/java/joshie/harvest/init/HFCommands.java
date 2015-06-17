package joshie.harvest.init;

import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.commands.HFCommandDay;
import joshie.harvest.core.commands.HFCommandGold;
import joshie.harvest.core.commands.HFCommandHelp;
import joshie.harvest.core.commands.HFCommandNewDay;
import joshie.harvest.core.commands.HFCommandReloadLang;
import joshie.harvest.core.commands.HFCommandSeason;
import joshie.harvest.core.commands.HFCommandWeather;
import joshie.harvest.core.commands.HFCommandYear;
import joshie.harvest.core.config.General;
import net.minecraftforge.common.MinecraftForge;

public class HFCommands {
    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        CommandManager.INSTANCE.registerCommand(new HFCommandHelp());
        CommandManager.INSTANCE.registerCommand(new HFCommandGold());
        CommandManager.INSTANCE.registerCommand(new HFCommandSeason());
        CommandManager.INSTANCE.registerCommand(new HFCommandDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandYear());
        CommandManager.INSTANCE.registerCommand(new HFCommandNewDay());
        CommandManager.INSTANCE.registerCommand(new HFCommandWeather());

        //Debug commands
        if (General.DEBUG_MODE) {
            CommandManager.INSTANCE.registerCommand(new HFCommandReloadLang());
        }
    }
}
