package joshie.harvest.plugins.crafttweaker.handler;

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

@ZenClass("mods.harvestfestival.Shipping")
public class Shipping {
    @ZenMethod
    public static void addShipping(IIngredient ingredient, long sellValue) {
        if (ingredient instanceof IItemStack || ingredient instanceof IOreDictEntry) {
            MineTweakerAPI.apply(new Add(ingredient, sellValue));
        }
    }

    private static class Add extends BaseOnce {
        private final long sellValue;
        private ItemStack stack;
        private Ore ore;

        public Add(IIngredient ingredient, long sellValue) {
            this.sellValue = sellValue;
            this.stack = asStack(ingredient);
            this.ore = Ore.of(asOre(ingredient));
        }

        @Override
        public String getDescription() {
            return "Added " + stack.getDisplayName() + " as shippable";
        }

        @Override
        public void applyOnce() {
            if (stack != null) HFApi.shipping.registerSellable(stack, sellValue);
            if (ore != null) HFApi.shipping.registerSellable(ore, sellValue);
        }
    }
}
