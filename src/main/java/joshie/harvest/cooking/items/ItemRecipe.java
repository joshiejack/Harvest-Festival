package joshie.harvest.cooking.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.HFRecipes;
import joshie.harvest.cooking.Recipe;
import joshie.harvest.core.base.ItemHFFML;
import joshie.harvest.core.util.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemRecipe extends ItemHFFML<ItemRecipe, Recipe> implements ICreativeSorted {
    public ItemRecipe() {
        super(FoodRegistry.REGISTRY, null);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() >= 10) {
            return Text.format(MODID + ".recipe.format", FoodRegistry.REGISTRY.getObjectById(stack.getItemDamage()).getDisplayName());
        } else {
            return DARK_GRAY + Text.translate("recipe.invalid");
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public Recipe getNullValue() {
        return HFRecipes.NULL_RECIPE;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }
}