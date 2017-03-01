package joshie.harvest.cooking.item;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.item.ItemHFRegistry;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static joshie.harvest.core.lib.HFModInfo.MODID;

//TODO in 0.7+ Remove ItemHFFML
public class ItemRecipe extends ItemHFRegistry<ItemRecipe, Recipe> implements ICreativeSorted {
    public ItemRecipe() {
        super("Recipe", CookingAPI.REGISTRY, Recipe.REGISTRY, HFTab.COOKING);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextHelper.format(MODID + ".recipe.format", getObjectFromStack(stack).getDisplayName());
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
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
    public Recipe getDefaultValue() {
        return Recipe.REGISTRY.get(new ResourceLocation("pancake_savoury"));
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 5000;
    }
}