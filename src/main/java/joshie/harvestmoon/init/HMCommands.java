package joshie.harvestmoon.init;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.core.commands.HMCommandDay;
import joshie.harvestmoon.core.commands.HMCommandGold;
import joshie.harvestmoon.core.commands.HMCommandHelp;
import joshie.harvestmoon.core.commands.HMCommandNewDay;
import joshie.harvestmoon.core.commands.HMCommandReloadLang;
import joshie.harvestmoon.core.commands.HMCommandSeason;
import joshie.harvestmoon.core.commands.HMCommandYear;
import joshie.harvestmoon.core.config.General;
import net.minecraftforge.common.MinecraftForge;

public class HMCommands {
    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(HMApi.COMMANDS);
        HMApi.COMMANDS.registerCommand(new HMCommandHelp());
        HMApi.COMMANDS.registerCommand(new HMCommandGold());
        HMApi.COMMANDS.registerCommand(new HMCommandSeason());
        HMApi.COMMANDS.registerCommand(new HMCommandDay());
        HMApi.COMMANDS.registerCommand(new HMCommandYear());
        HMApi.COMMANDS.registerCommand(new HMCommandNewDay());

        //Debug commands
        if (General.DEBUG_MODE) {
            HMApi.COMMANDS.registerCommand(new HMCommandReloadLang());
        }
    }
}
