package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.plugins.crafttweaker.command.HFCommandNPC;
import joshie.harvest.plugins.crafttweaker.command.HFCommandPurchasable;
import joshie.harvest.plugins.crafttweaker.command.HFCommandShops;
import joshie.harvest.plugins.crafttweaker.handler.Shipping;
import joshie.harvest.plugins.crafttweaker.handler.Shops;
import minetweaker.MineTweakerAPI;

@HFLoader(mods = "MineTweaker3")
public class CraftTweaker {
    public static void init() {
        MineTweakerAPI.registerClass(Shipping.class);
        MineTweakerAPI.registerClass(Shops.class);
        CommandManager.INSTANCE.registerCommand(new HFCommandShops());
        CommandManager.INSTANCE.registerCommand(new HFCommandPurchasable());
        CommandManager.INSTANCE.registerCommand(new HFCommandNPC());
    }

    public static void logError(String format) {
        MineTweakerAPI.logError(format);
    }
}
