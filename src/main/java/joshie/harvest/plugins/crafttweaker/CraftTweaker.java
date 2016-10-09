package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.shops.command.HFCommandShops;
import minetweaker.MineTweakerAPI;

@HFLoader(mods = "MineTweaker3")
public class CraftTweaker {
    public static void init() {
        MineTweakerAPI.registerClass(Shipping.class);
        MineTweakerAPI.registerClass(Shops.class);
        CommandManager.INSTANCE.registerCommand(new HFCommandShops());
    }
}
