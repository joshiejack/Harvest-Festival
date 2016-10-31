package joshie.harvest.cooking.item;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.cooking.recipe.HFRecipes;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFoodFML;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.cooking.recipe.RecipeBuilder.FOOD_LEVEL;
import static joshie.harvest.cooking.recipe.RecipeBuilder.SATURATION_LEVEL;
import static joshie.harvest.cooking.recipe.RecipeBuilder.SELL_VALUE;
import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemMeal extends ItemHFFoodFML<ItemMeal, Recipe> implements IShippable {
    public ItemMeal() {
        super(Recipe.REGISTRY, HFTab.COOKING);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Recipe impl = stack.hasTagCompound() ? Recipe.REGISTRY.getValues().get(Math.max(0, Math.min(Recipe.REGISTRY.getValues().size(), stack.getItemDamage()))): null;
        return impl != null ? impl.getDisplayName(): DARK_GRAY + TextHelper.localize(Utensil.getUtensilFromIndex(stack.getItemDamage()).getUnlocalizedName());
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger(FOOD_LEVEL) : 0;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getFloat(SATURATION_LEVEL) : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (debug) {
            if (stack.hasTagCompound()) {
                list.add(TextHelper.translate("meal.hunger") + " : " + stack.getTagCompound().getInteger(FOOD_LEVEL));
                list.add(TextHelper.translate("meal.sat") + " : " + stack.getTagCompound().getFloat(SATURATION_LEVEL));
                list.add(TextHelper.translate("meal.sell") + " : " + stack.getTagCompound().getLong(SELL_VALUE));
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (stack.hasTagCompound() && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            player.getFoodStats().addStats(this, stack);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return getObjectFromStack(stack).getEatTimer();
        } else return super.getMaxItemUseDuration(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return getObjectFromStack(stack).getAction();
        } else return EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.canEat(false)) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }

    @Override
    public Recipe getNullValue() {
        return HFRecipes.NULL_RECIPE;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (stack.getTagCompound() == null) return 0;
        else return stack.getTagCompound().getLong("SellValue");
    }

    @Override
    public ItemStack getCreativeStack(Item item, Recipe recipe) {
        ArrayList<IngredientStack> stacks = new ArrayList<>();
        stacks.addAll(recipe.getRequired());
        if (recipe.getOptional().size() > 0)stacks.addAll(recipe.getOptional());
        return RecipeMaker.BUILDER.build(recipe, stacks).get(0);
    }

    @Override
    public ItemStack getStackFromObject(Recipe recipe) {
        return getCreativeStack(this, recipe);
    }

    @Override
    public ItemStack getStackFromResource(ResourceLocation resource) {
        return getStackFromObject(registry.getValue(resource));
    }

    @Override
    public boolean shouldDisplayInCreative(Recipe recipe) {
        return recipe.supportsNBTData();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }
}