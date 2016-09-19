package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.shops.purchaseable.PurchaseableBuilder;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static joshie.harvest.plugins.crafttweaker.BaseUndoable.asStack;

@ZenClass("mods.harvestfestival.Shops")
public class Shops {
    @ZenMethod
    public static void addSellable(String shop, IItemStack sellable, long cost) {
        MineTweakerAPI.apply(new AddSellable(shop, asStack(sellable), cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AddSellable extends BaseUndoable {
        protected final IShop shop;
        protected final ItemStack stack;
        protected final long cost;

        public AddSellable(String shop, ItemStack stack, long cost) {
            this.shop = HFApi.shops.getShop(new ResourceLocation(shop));
            this.stack = stack;
            this.cost = cost;
        }

        @Override
        public String getDescription() {
            return "Adding " + stack.getDisplayName() + " to the Carpenter's shop.";
        }

        @Override
        public void applyOnce() {
            shop.addItem(cost, stack);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addSellableToBuilder(IItemStack sellable, int wood, int stone, long cost) {
        MineTweakerAPI.apply(new AddBuilderSellable(asStack(sellable), wood, stone, cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AddBuilderSellable extends AddSellable {
        protected final int stone;
        protected final int wood;

        public AddBuilderSellable(ItemStack stack, int stone, int wood, long cost) {
            super("harvestfestival:carpenter", stack, cost);
            this.stone = stone;
            this.wood = wood;
        }

        @Override
        public String getDescription() {
            return "Adding " + stack.getDisplayName() + " to the Carpenter's shop.";
        }

        @Override
        public void applyOnce() {
            shop.addItem(new PurchaseableBuilder(cost, stone, wood, stack));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
