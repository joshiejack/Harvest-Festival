package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.gastronomy.GastronomySounds;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import uk.joshiejack.penguinlib.util.helpers.minecraft.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemRecipe extends ItemSingular {
    public ItemRecipe() {
        super(new ResourceLocation(MODID, "recipe"));
        setHasSubtypes(true);
        setCreativeTab(Gastronomy.TAB);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        ItemStack internal = new ItemStack(NBTHelper.getItemNBT(stack));
        return internal.isEmpty() ? super.getItemStackDisplayName(stack) : I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", internal.getDisplayName());
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        ItemStack internal = new ItemStack(NBTHelper.getItemNBT(held));
        if (!internal.isEmpty() && Cooker.learnRecipe(player, internal)) {
            world.playSound(player.posX, player.posY, player.posZ, GastronomySounds.RECIPE, SoundCategory.NEUTRAL, 0.8F, 1F, true);
            held.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public ItemStack getRecipeWithStack(Recipe recipe) {
        NBTTagCompound tag = new NBTTagCompound();
        recipe.getResult().writeToNBT(tag);
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            Recipe.PRIORITY_RECIPES.values().forEach((recipe -> items.add(getRecipeWithStack(recipe))));
            Recipe.RECIPES.values().forEach((recipe -> items.add(getRecipeWithStack(recipe))));
        }
    }
}
