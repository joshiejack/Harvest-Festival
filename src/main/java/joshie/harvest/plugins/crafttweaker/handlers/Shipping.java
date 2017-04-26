package joshie.harvest.plugins.crafttweaker.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Ore;
import joshie.harvest.plugins.crafttweaker.base.BaseOnce;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;

import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asOre;
import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asStack;

@ZenClass("mods.harvestfestival.Shipping")
public class Shipping {
    @ZenMethod
    @SuppressWarnings("unused")
    public static void addShipping(IIngredient ingredient, long sellValue) {
        if (ingredient instanceof IItemStack || ingredient instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new Add(ingredient, sellValue));
        }
    }

    private static class Add extends BaseOnce {
        private final long sellValue;
        @Nonnull
        private ItemStack stack;
        private Ore ore;

        public Add(IIngredient ingredient, long sellValue) {
            this.sellValue = sellValue;
            this.stack = asStack(ingredient);
            String name = asOre(ingredient);
            if (name != null) this.ore = Ore.of(name);
        }

        @Override
        public String getDescription() {
            if (ore != null) return "Added " + ore.getOre() + " as shippable";
            else if (!stack.isEmpty()) return "Added " + stack.getDisplayName() + " as shippable";
            else return "Added nothing";
        }

        @Override
        public void applyOnce() {
            if (!stack.isEmpty()) HFApi.shipping.registerSellable(stack, sellValue);
            if (ore != null) HFApi.shipping.registerSellable(ore, sellValue);
        }
    }
}