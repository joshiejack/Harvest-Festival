package uk.joshiejack.husbandry.world;

import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemTreat;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class HusbandryTrades {
    public static class Generic implements EntityVillager.ITradeList {
        @Override
        public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipes, @Nonnull Random random) {
            recipes.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1), HusbandryItems.TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC, 5)));
        }
    }

    public static class Special implements EntityVillager.ITradeList {
        @Override
        public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipes, @Nonnull Random random) {
            for (ItemTreat.Treat treat: ItemTreat.Treat.values()) {
                if (treat != ItemTreat.Treat.GENERIC) {
                    recipes.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), HusbandryItems.TREAT.getStackFromEnum(treat, 3)));
                }
            }
        }
    }
}