package joshie.harvest.cooking.items;

import joshie.harvest.api.cooking.IAltItem;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.HFRecipes;
import joshie.harvest.cooking.Recipe;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFFML;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemMeal extends ItemHFFML<ItemMeal, Recipe> implements ICreativeSorted, IAltItem {
    public ItemMeal() {
        super(FoodRegistry.REGISTRY, HFTab.COOKING);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() >= 10) {
            return FoodRegistry.REGISTRY.getObjectById(stack.getItemDamage()).getDisplayName();
        } else {
            int meta = Math.min(Utensil.values().length - 1, stack.getItemDamage());
            return DARK_GRAY + Text.translate("meal.burnt." + Utensil.values()[meta].name().replace("_", ".").toLowerCase());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (HFCore.DEBUG_MODE && debug) {
            if (stack.hasTagCompound()) {
                list.add(Text.translate("meal.hunger") + " : " + stack.getTagCompound().getInteger("FoodLevel"));
                list.add(Text.translate("meal.sat") + " : " + stack.getTagCompound().getFloat("FoodSaturation"));
                list.add(Text.translate("meal.exhaust") + " : " + stack.getTagCompound().getInteger("FoodExhaustion"));
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
    public Recipe getNullValue() {
        return HFRecipes.NULL_RECIPE;
    }

    //Apply all the relevant information about this meal to the meal stack
    public ItemStack cook(Recipe recipe, IMeal meal) {
        ItemStack stack = new ItemStack(this, 1, registry.getIDForObject(recipe));
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("FoodLevel", meal.getHunger());
        stack.getTagCompound().setFloat("FoodSaturation", meal.getSaturation());
        stack.getTagCompound().setFloat("FoodExhaustion", meal.getExhaustion());
        stack.getTagCompound().setBoolean("IsDrink", meal.isDrink());
        stack.getTagCompound().setInteger("EatTime", meal.getEatTime());
        return stack;
    }

    @Override
    public ItemStack getCreativeStack(Item item, Recipe recipe) {
        return cook(recipe, recipe.getBestMeal());
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }

    @Override
    public ItemStack getAlternativeWhenCooking(ItemStack stack) {
        Recipe recipe = FoodRegistry.REGISTRY.getObjectById(stack.getItemDamage());
        if (recipe != null) {
            return recipe.getMeal().getAlternativeItem();
        }

        return null;
    }
}