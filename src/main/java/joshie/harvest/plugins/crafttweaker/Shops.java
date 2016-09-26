package joshie.harvest.plugins.crafttweaker;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.shops.purchasable.PurchasableBuilder;
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
    public static void addPurchasable(String shop, IItemStack sellable, long cost) {
        MineTweakerAPI.apply(new AddPurchasable(shop, asStack(sellable), cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AddPurchasable extends BaseUndoable {
        protected final IShop shop;
        protected final ItemStack stack;
        protected final long cost;

        public AddPurchasable(String shop, ItemStack stack, long cost) {
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
    public static void addPurchasableToBuilder(IItemStack sellable, int wood, int stone, long cost) {
        MineTweakerAPI.apply(new AddBuilderPurchasable(asStack(sellable), wood, stone, cost));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class AddBuilderPurchasable extends AddPurchasable {
        protected final int stone;
        protected final int wood;

        public AddBuilderPurchasable(ItemStack stack, int stone, int wood, long cost) {
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
            shop.addItem(new PurchasableBuilder(cost, stone, wood, stack));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
