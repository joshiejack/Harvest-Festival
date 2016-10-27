package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.helpers.ConfigHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.plugins.crafttweaker.handlers.Crops;
import joshie.harvest.plugins.crafttweaker.handlers.Shipping;
import joshie.harvest.plugins.crafttweaker.handlers.Shops;
import joshie.harvest.shops.command.HFCommandShops;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.mc1102.brackets.ItemBracketHandler;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.io.File;

@HFLoader(mods = "MineTweaker3")
public class CraftTweaker {
    public static void init() {
        /** Attempt to load in the crops before world **/
        File directory = new File(ConfigHelper.getConfig().getConfigFile().getParentFile(), "harvestfestival");
        boolean exists = directory.exists();
        if (!exists){
            exists = directory.mkdir();
        }

        if(exists) {
            MineTweakerAPI.registerBracketHandler(new ItemBracketHandler());
            ItemBracketHandler.rebuildItemRegistry();
            MineTweakerAPI.registerClass(Crops.class);
            MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderDirectory(directory));
            MineTweakerImplementationAPI.reload();
        } else { /**/ }

        MineTweakerAPI.registerClass(Shipping.class);
        MineTweakerAPI.registerClass(Shops.class);
        CommandManager.INSTANCE.registerCommand(new HFCommandShops());
    }

    public static Block asBlock(IItemStack ingredient) {
        ItemStack stack = asStack(ingredient);
        if (stack != null && stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock)stack.getItem()).getBlock();
        } else return null;
    }

    //Helpers
    public static ItemStack asStack(IIngredient ingredient) {
        return ingredient.getInternal() instanceof ItemStack ? (ItemStack) ingredient.getInternal() : null;
    }

    public static String asOre(IIngredient ingredient) {
        return ingredient.getInternal() instanceof IOreDictEntry ? ((IOreDictEntry) ingredient).getName() : null;
    }
}
