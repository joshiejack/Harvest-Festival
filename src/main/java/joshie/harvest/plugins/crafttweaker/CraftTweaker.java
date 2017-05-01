package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.plugins.crafttweaker.command.HFCommandNPC;
import joshie.harvest.plugins.crafttweaker.command.HFCommandPurchasable;
import joshie.harvest.plugins.crafttweaker.command.HFCommandShops;
import joshie.harvest.plugins.crafttweaker.handlers.*;
import joshie.harvest.plugins.crafttweaker.wrappers.RequirementItemWrapper;
import joshie.harvest.plugins.crafttweaker.wrappers.RequirementOreWrapper;
import minetweaker.IBracketHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@HFLoader(mods = "MineTweaker3")
public class CraftTweaker {
    private static IBracketHandler getItemBracketHandler() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (IBracketHandler) Class.forName("minetweaker.mc1102.brackets.ItemBracketHandler").newInstance();
    }

    private static void rebuildItemRegistry(IBracketHandler handler) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        handler.getClass().getMethod("rebuildItemRegistry").invoke(null);
    }

    public static void init() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        /** Attempt to load in the crops before world **/
        File directory = new File("scripts", "harvestfestival");
        boolean exists = directory.exists();
        if (!exists){
            exists = directory.mkdir();
        }

        if (exists) {
            IBracketHandler handler = getItemBracketHandler();
            MineTweakerAPI.registerBracketHandler(handler);
            rebuildItemRegistry(handler);
            MineTweakerAPI.registerClass(Crops.class);
            MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderDirectory(directory));
            MineTweakerImplementationAPI.reload();
        }

        MineTweakerAPI.registerClass(Blacklist.class);
        MineTweakerAPI.registerClass(Shipping.class);
        MineTweakerAPI.registerClass(Shops.class);
        MineTweakerAPI.registerClass(Gifting.class);
        CommandManager.INSTANCE.addSubcommand(new HFCommandNPC());
        CommandManager.INSTANCE.addSubcommand(new HFCommandPurchasable());
        CommandManager.INSTANCE.addSubcommand(new HFCommandShops());
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
