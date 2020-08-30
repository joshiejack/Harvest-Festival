package joshie.harvest.plugins.crafttweaker.handlers;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.plugins.crafttweaker.CraftTweaker;
import joshie.harvest.plugins.crafttweaker.base.BaseOnce;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asOre;
import static joshie.harvest.plugins.crafttweaker.CraftTweaker.asStack;

@ZenClass("mods.harvestfestival.Gifts")
public class Gifting {
    @ZenMethod
    @SuppressWarnings("unused")
    public static void addGift(IIngredient ingredient, String category) {
        if (ingredient instanceof IItemStack || ingredient instanceof IOreDictEntry) {
            try {
                GiftCategory theCategory = GiftCategory.valueOf(category.toUpperCase());
                CraftTweakerAPI.apply(new Add(ingredient, theCategory));
            } catch (IllegalArgumentException ex) { CraftTweaker.logError(String.format("No category with the name %s could be found", category)); }
        }
    }

    private static class Add extends BaseOnce {
        private final GiftCategory category;
        private final Object object;

        public Add(IIngredient ingredient, GiftCategory category) {
            this.category = category;
            String name = asOre(ingredient);
            if (name != null) this.object = Ore.of(name);
            else this.object = asStack(ingredient);
        }

        private String getNameForObject() {
            return object instanceof Ore ? ((Ore)object).getOre() : object instanceof ItemStack ? ((ItemStack)object).getDisplayName() : " nothing ";
        }

        @Override
        public String getDescription() {
            return "Categorised " + getNameForObject() + " as the gift type " + category.name();
        }

        @Override
        public void applyOnce() {
            HFApi.npc.getGifts().setCategory(object, category);
        }
    }
}
