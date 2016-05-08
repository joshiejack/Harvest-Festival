package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.IMeal;
import joshie.harvest.api.cooking.IMealProvider;
import joshie.harvest.api.cooking.IMealRecipe;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.creativetab.CreativeTabs;
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

import java.util.HashSet;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.DARK_GRAY;

public class ItemMeal extends ItemHFBaseMeta implements IMealProvider, ICreativeSorted {
    public ItemMeal() {
        super(HFTab.COOKING);
    }

    public static String getMeal(ItemStack stack) {
        return stack.getTagCompound().getString("FoodName");
    }

    //Irrelevant since we overwrite them, but it needs it specified
    @Override
    public int getMetaCount() {
        return 1;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return Translate.translate("meal." + getMeal(stack));
        } else {
            int meta = Math.min(Utensil.values().length - 1, stack.getItemDamage());
            return DARK_GRAY + Translate.translate("meal.burnt." + Utensil.values()[meta].name().replace("_", ".").toLowerCase());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (General.DEBUG_MODE) {
            if (stack.hasTagCompound()) {
                list.add(Translate.translate("meal.level") + " : " + stack.getTagCompound().getInteger("FoodLevel"));
                list.add(Translate.translate("meal.sat") + " : " + stack.getTagCompound().getFloat("FoodSaturation"));
                list.add(Translate.translate("meal.stamina") + " : " + stack.getTagCompound().getInteger("FoodStamina"));
                list.add(Translate.translate("meal.fatigue") + " : " + stack.getTagCompound().getInteger("FoodFatigue"));
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (stack.hasTagCompound() && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            int level = stack.getTagCompound().getInteger("FoodLevel");
            float sat = stack.getTagCompound().getFloat("FoodSaturation");
            int stamina = stack.getTagCompound().getInteger("FoodStamina");
            int fatigue = stack.getTagCompound().getInteger("FoodFatigue");
            HFTrackers.getPlayerTracker(player).getStats().affectStats(stamina, fatigue);
            player.getFoodStats().addStats(level, sat);
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
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }
    }

    //Apply all the relevant information about this meal to the meal stack
    public static ItemStack cook(ItemStack stack, IMeal meal) {
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("FoodName", meal.getUnlocalizedName());
        stack.getTagCompound().setInteger("FoodLevel", meal.getHunger());
        stack.getTagCompound().setFloat("FoodSaturation", meal.getSaturation());
        stack.getTagCompound().setInteger("FoodStamina", meal.getStamina());
        stack.getTagCompound().setInteger("FoodFatigue", meal.getFatigue());
        stack.getTagCompound().setBoolean("IsDrink", meal.isDrink());
        stack.getTagCompound().setInteger("EatTime", meal.getEatTime());
        return stack;
    }

    @Override
    public String getMealName(ItemStack stack) {
        if (!stack.hasTagCompound()) return "burnt";
        return stack.getTagCompound().getString("FoodName");
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.COOKING};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creative, List<ItemStack> list) {
        HashSet<IMeal> added = new HashSet<IMeal>();
        for (IMealRecipe recipe : HFApi.COOKING.getRecipes()) {
            IMeal best = recipe.getBestMeal();
            if (added.add(best)) {
                list.add(cook(new ItemStack(item), best));
            }
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 100;
    }
}