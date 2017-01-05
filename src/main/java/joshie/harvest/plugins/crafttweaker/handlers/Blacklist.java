package joshie.harvest.plugins.crafttweaker.handlers;

import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.plugins.crafttweaker.CraftTweaker;
import joshie.harvest.plugins.crafttweaker.base.BaseUndoable;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asStack;

@ZenClass("mods.harvestfestival.Blacklist")
public class Blacklist {
    @ZenMethod
    @SuppressWarnings("unused")
    public static void blacklistSeeds(IItemStack drop) {
        ItemStack stack = asStack(drop);
        if (stack == null) CraftTweaker.logError("Could not blacklist seeds as the item was null");
        else MineTweakerAPI.apply(new BlacklistSeeds(stack));
    }

    private static class BlacklistSeeds extends BaseUndoable {
        private final ItemStack item;

        BlacklistSeeds(ItemStack drop) {
            this.item = drop;
        }

        @Override
        public String getDescription() {
            return "Blacklisting the seeds " + item.getDisplayName();
        }

        @Override
        public void apply() {
            DisableHandler.SEEDS_BLACKLIST.register(item);
        }

        @Override
        public void undo() {
            DisableHandler.SEEDS_BLACKLIST.unregister(item);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void blacklistHoe(IItemStack drop) {
        ItemStack stack = asStack(drop);
        if (stack == null) CraftTweaker.logError("Could not blacklist seeds as the item was null");
        else MineTweakerAPI.apply(new BlacklistHoe(stack));
    }

    private static class BlacklistHoe extends BaseUndoable {
        private final ItemStack item;

        BlacklistHoe(ItemStack drop) {
            this.item = drop;
        }

        @Override
        public String getDescription() {
            return "Blacklisting the hoe " + item.getDisplayName();
        }

        @Override
        public void apply() {
            DisableHandler.HOE_BLACKLIST.register(item);
        }

        @Override
        public void undo() {
            DisableHandler.HOE_BLACKLIST.unregister(item);
        }
    }
}
