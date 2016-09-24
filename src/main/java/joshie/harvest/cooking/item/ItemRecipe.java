package joshie.harvest.cooking.item;

import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.recipe.HFRecipes;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.core.util.ICreativeSorted;
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

public class ItemRecipe extends ItemHFFML<ItemRecipe, MealImpl> implements ICreativeSorted {
    public ItemRecipe() {
        super(CookingAPI.REGISTRY, HFTab.COOKING);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int id = Math.max(0, Math.min(CookingAPI.REGISTRY.getValues().size(), stack.getItemDamage()));
        return Text.format(MODID + ".recipe.format", CookingAPI.REGISTRY.getValues().get(id).getDisplayName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        MealImpl recipe = getObjectFromStack(stack);
        if (recipe != null && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().learnRecipe(recipe)) {
            if (!player.capabilities.isCreativeMode) stack.stackSize--; //Decrease the stack
            world.playSound(player.posX, player.posY, player.posZ, HFSounds.RECIPE, SoundCategory.NEUTRAL, 0.8F, 1F, true);
            ChatHelper.displayChat(Text.translate("meal.learnt") + " " + TextFormatting.YELLOW + recipe.getDisplayName());
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public MealImpl getNullValue() {
        return HFRecipes.NULL_RECIPE;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 5000;
    }
}