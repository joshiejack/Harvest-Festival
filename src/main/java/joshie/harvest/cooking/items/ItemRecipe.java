package joshie.harvest.cooking.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.recipe.HFRecipes;
import joshie.harvest.cooking.recipe.Recipe;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.ItemHFFML;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.core.util.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemRecipe extends ItemHFFML<ItemRecipe, Recipe> implements ICreativeSorted {
    public ItemRecipe() {
        super(FoodRegistry.REGISTRY, HFTab.COOKING);
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
        Recipe recipe = getObjectFromStack(stack);
        if (recipe != null && HFTrackers.getPlayerTracker(player).getTracking().learnRecipe(recipe)) {
            if (!player.capabilities.isCreativeMode) stack.stackSize--; //Decrease the stack
            world.playSound(player.posX, player.posY, player.posZ, HFSounds.RECIPE, SoundCategory.NEUTRAL, 0.8F, 1F, true);
            ChatHelper.displayChat(Text.translate("meal.learnt") + TextFormatting.YELLOW + recipe.getDisplayName());
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public Recipe getNullValue() {
        return HFRecipes.NULL_RECIPE;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 5000;
    }
}