package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.core.lib.HMModInfo;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class HMCommandReloadLang extends HMCommandBase {
    @Override
    public String getCommandName() {
        return "lang";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        System.out.println("PROCESSED");
        for (ModContainer mod: Loader.instance().getActiveModList()) {
            if (mod.getModId().equals(HMModInfo.MODID)) {
                LanguageRegistry.instance().loadLanguagesFor(mod, Side.SERVER);
                break;
            }
        }
        
        return true;
    }
}
