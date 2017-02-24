package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.helpers.ConfigHelper;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.plugins.crafttweaker.command.HFCommandNPC;
import joshie.harvest.plugins.crafttweaker.command.HFCommandPurchasable;
import joshie.harvest.plugins.crafttweaker.command.HFCommandShops;
import joshie.harvest.plugins.crafttweaker.handlers.Blacklist;
import joshie.harvest.plugins.crafttweaker.handlers.Crops;
import joshie.harvest.plugins.crafttweaker.handlers.Shipping;
import joshie.harvest.plugins.crafttweaker.handlers.Shops;
import joshie.harvest.plugins.crafttweaker.wrappers.RequirementItemWrapper;
import joshie.harvest.plugins.crafttweaker.wrappers.RequirementOreWrapper;
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
import java.util.ArrayList;

@HFLoader(mods = "MineTweaker3")
public class CraftTweaker {
    public static void init() {
        /** Attempt to load in the crops before world **/
        File directory = new File(ConfigHelper.getConfig().getConfigFile().getParentFile(), "harvestfestival");
        boolean exists = directory.exists();
        if (!exists){
            exists = directory.mkdir();
        }

        if (exists) {
            MineTweakerAPI.registerBracketHandler(new ItemBracketHandler());
            ItemBracketHandler.rebuildItemRegistry();
            MineTweakerAPI.registerClass(Crops.class);
            MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderDirectory(directory));
            MineTweakerImplementationAPI.reload();
        } else { /**/ }

        MineTweakerAPI.registerClass(Blacklist.class);
        MineTweakerAPI.registerClass(Shipping.class);
        MineTweakerAPI.registerClass(Shops.class);
        CommandManager.INSTANCE.registerCommand(new HFCommandNPC());
        CommandManager.INSTANCE.registerCommand(new HFCommandPurchasable());
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
        return ingredient != null && ingredient.getInternal() instanceof ItemStack ? (ItemStack) ingredient.getInternal() : null;
    }

    public static String asOre(IIngredient ingredient) {
        return ingredient != null && ingredient.getInternal() instanceof IOreDictEntry ? ((IOreDictEntry) ingredient).getName() : null;
    }

    public static IRequirement[] asRequirements(IIngredient[] ingredients) {
        if (ingredients == null) return null;
        ArrayList<IRequirement> stacks = new ArrayList<>();
        for (IIngredient ingredient : ingredients) {
            if (ingredient instanceof IOreDictEntry) {
                stacks.add(new RequirementOreWrapper(asOre(ingredient), ingredient.getAmount()));
            } else if (ingredient.getInternal() instanceof ItemStack) {
                stacks.add(new RequirementItemWrapper(asStack(ingredient), ingredient.getAmount()));
            }
        }

        return stacks.toArray(new IRequirement[stacks.size()]);
    }

    public static void logError(String message) {
        MineTweakerAPI.logError(message);
    }
}
