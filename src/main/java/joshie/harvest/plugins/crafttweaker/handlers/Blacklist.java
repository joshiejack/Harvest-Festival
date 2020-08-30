package joshie.harvest.plugins.crafttweaker.handlers;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.plugins.crafttweaker.CraftTweaker;
import joshie.harvest.plugins.crafttweaker.base.BaseOnce;
import joshie.harvest.plugins.crafttweaker.base.BaseUndoable;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asStack;

@ZenClass("mods.harvestfestival.Blacklist")
public class Blacklist {
    @ZenMethod
    @SuppressWarnings("unused")
    public static void blacklistSeeds(IItemStack drop) {
        ItemStack stack = asStack(drop);
        if (stack.isEmpty()) CraftTweaker.logError("Could not blacklist seeds as the item was null");
        else CraftTweakerAPI.apply(new BlacklistSeeds(stack));
    }

    private static class BlacklistSeeds extends BaseUndoable {
        @Nonnull
        private final ItemStack item;

        BlacklistSeeds(@Nonnull ItemStack drop) {
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
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void blacklistHoe(IItemStack drop) {
        ItemStack stack = asStack(drop);
        if (stack.isEmpty()) CraftTweaker.logError("Could not blacklist seeds as the item was null");
        else CraftTweakerAPI.apply(new BlacklistHoe(stack));
    }

    private static class BlacklistHoe extends BaseUndoable {
        @Nonnull
        private final ItemStack item;

        BlacklistHoe(@Nonnull ItemStack drop) {
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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void blacklistGiftable(IItemStack drop) {
        ItemStack stack = asStack(drop);
        if (stack.isEmpty()) CraftTweaker.logError("Could not prevent an item from being gifted as it was null");
        else CraftTweakerAPI.apply(new BlacklistGifted(stack));
    }

    private static class BlacklistGifted extends BaseOnce {
        @Nonnull
        private final ItemStack item;

        BlacklistGifted(@Nonnull ItemStack drop) {
            this.item = drop;
        }

        @Override
        public String getDescription() {
            return "Preventing " + item.getDisplayName() + " from being giftable";
        }

        @Override
        public void applyOnce() {
            HFApi.npc.getGifts().addToBlacklist(item);
        }
    }
}
