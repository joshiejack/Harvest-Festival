package joshie.harvest.cooking.item;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.recipe.HFRecipes;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ItemRecipe extends ItemHFFML<ItemRecipe, Recipe> implements ICreativeSorted {
    public ItemRecipe() {
        super(Recipe.REGISTRY, HFTab.COOKING);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int id = Math.max(0, Math.min(Recipe.REGISTRY.getValues().size() - 1, stack.getItemDamage()));
        return TextHelper.format(MODID + ".recipe.format", Recipe.REGISTRY.getValues().get(id).getDisplayName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        Recipe recipe = getObjectFromStack(stack);
        if (recipe != null && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().learnRecipe(recipe)) {
            if (!player.capabilities.isCreativeMode) stack.stackSize--; //Decrease the stack
            world.playSound(player.posX, player.posY, player.posZ, HFSounds.RECIPE, SoundCategory.NEUTRAL, 0.8F, 1F, true);
            if (world.isRemote) ChatHelper.displayChat(TextHelper.translate("meal.learnt") + " " + TextFormatting.YELLOW + recipe.getDisplayName());
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