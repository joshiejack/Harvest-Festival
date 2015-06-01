package joshie.harvest.core.commands;

import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.command.ICommandSender;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

public class HFCommandReloadLang extends HFCommandBase {
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
            if (mod.getModId().equals(HFModInfo.MODID)) {
                LanguageRegistry.instance().loadLanguagesFor(mod, Side.SERVER);
                break;
            }
        }
        
        return true;
    }
}
