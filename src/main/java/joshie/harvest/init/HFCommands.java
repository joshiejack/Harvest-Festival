package joshie.harvest.init;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.commands.HFCommandDay;
import joshie.harvest.core.commands.HFCommandGold;
import joshie.harvest.core.commands.HFCommandHelp;
import joshie.harvest.core.commands.HFCommandNewDay;
import joshie.harvest.core.commands.HFCommandReloadLang;
import joshie.harvest.core.commands.HFCommandSeason;
import joshie.harvest.core.commands.HFCommandYear;
import joshie.harvest.core.config.General;
import net.minecraftforge.common.MinecraftForge;

public class HFCommands {
    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(HFApi.COMMANDS);
        HFApi.COMMANDS.registerCommand(new HFCommandHelp());
        HFApi.COMMANDS.registerCommand(new HFCommandGold());
        HFApi.COMMANDS.registerCommand(new HFCommandSeason());
        HFApi.COMMANDS.registerCommand(new HFCommandDay());
        HFApi.COMMANDS.registerCommand(new HFCommandYear());
        HFApi.COMMANDS.registerCommand(new HFCommandNewDay());

        //Debug commands
        if (General.DEBUG_MODE) {
            HFApi.COMMANDS.registerCommand(new HFCommandReloadLang());
        }
    }
}
