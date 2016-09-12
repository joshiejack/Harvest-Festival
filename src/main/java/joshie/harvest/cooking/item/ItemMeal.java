package joshie.harvest.cooking.item;

import joshie.harvest.api.cooking.IAltItem;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.recipe.HFRecipes;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFML;
import joshie.harvest.core.util.Text;
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

import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemMeal extends ItemHFFML<ItemMeal, MealImpl> implements IAltItem, IShippable {
    public ItemMeal() {
        super(CookingAPI.REGISTRY, HFTab.COOKING);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        MealImpl impl = stack.hasTagCompound() ? CookingAPI.REGISTRY.getValues().get(stack.getItemDamage()): null;
        return impl != null ? impl.getDisplayName(): DARK_GRAY + Text.localize(Utensil.getUtensilFromIndex(stack.getItemDamage()).getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (debug && HFCore.DEBUG_MODE) {
            if (stack.hasTagCompound()) {
                list.add(Text.translate("meal.hunger") + " : " + stack.getTagCompound().getInteger("FoodLevel"));
                list.add(Text.translate("meal.sat") + " : " + stack.getTagCompound().getFloat("FoodSaturation"));
                list.add(Text.translate("meal.exhaust") + " : " + stack.getTagCompound().getInteger("FoodExhaustion"));
                list.add(Text.translate("meal.sell") + " : " + stack.getTagCompound().getLong("SellValue"));
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (stack.hasTagCompound() && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            int hunger = stack.getTagCompound().getInteger("FoodLevel");
            float saturation = stack.getTagCompound().getFloat("FoodSaturation");
            float exhaustion = stack.getTagCompound().getFloat("FoodExhaustion");
            player.getFoodStats().addStats(hunger, saturation);
            player.getFoodStats().addExhaustion(exhaustion);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.getTagCompound().getInteger("EatTime");
        } else return super.getMaxItemUseDuration(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.getTagCompound().getBoolean("IsDrink") ? EnumAction.DRINK : EnumAction.EAT;
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
    public MealImpl getNullValue() {
        return HFRecipes.NULL_RECIPE;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (stack.getTagCompound() == null) return 0;
        else return stack.getTagCompound().getLong("SellValue");
    }

    @Override
    public ItemStack getCreativeStack(Item item, MealImpl recipe) {
        return recipe.cook(recipe.getBestMeal());
    }

    @Override
    public ItemStack getStackFromObject(MealImpl recipe) {
        return getCreativeStack(this, recipe);
    }

    @Override
    public ItemStack getStackFromResource(ResourceLocation resource) {
        return getStackFromObject(registry.getValue(resource));
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }

    @Override
    public ItemStack getAlternativeWhenCooking(ItemStack stack) {
        MealImpl recipe = CookingAPI.REGISTRY.getValues().get(stack.getItemDamage());
        if (recipe != null) {
            return recipe.getAlternativeItem();
        }

        return null;
    }
}