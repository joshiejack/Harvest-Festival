package uk.joshiejack.horticulture.world;

import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.item.ItemCrop;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class FarmerTrades implements EntityVillager.ITradeList {
    @Override
    public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipes, @Nonnull Random random) {
        recipes.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 5), HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.CABBAGE, 1)));
        recipes.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 7), HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.PINEAPPLE, 1)));
        recipes.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 3), HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.GREEN_PEPPER, 1)));
        recipes.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 4), HorticultureItems.SEEDS.getStackFromEnum(ItemCrop.Crops.GRAPE, 1)));
    }
}