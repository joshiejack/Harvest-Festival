package joshie.harvestmoon.items;

import static joshie.harvestmoon.helpers.PlayerHelper.affectStats;
import static joshie.harvestmoon.lib.HMModInfo.MEALPATH;

import java.util.List;

import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.cooking.Meal;
import joshie.harvestmoon.cooking.Recipe;
import joshie.harvestmoon.lib.CropMeta;
import joshie.harvestmoon.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMeal extends ItemHMMeta {
    //Irrelevant since we overwrite them, but it needs it specified
    @Override
    public int getMetaCount() {
        return 1;
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return Translate.translate("meal." + stack.stackTagCompound.getString("FoodName"));
        } else return super.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {
        if (stack.hasTagCompound() && debug) {
            list.add(Translate.translate("meal.level") + " : " + stack.stackTagCompound.getInteger("FoodLevel"));
            list.add(Translate.translate("meal.sat") + " : " + stack.stackTagCompound.getFloat("FoodSaturation"));
            list.add(Translate.translate("meal.stamina") + " : " + stack.stackTagCompound.getInteger("FoodStamina"));
            list.add(Translate.translate("meal.fatigue") + " : " + stack.stackTagCompound.getInteger("FoodFatigue"));
        }
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (stack.hasTagCompound()) {
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            int level = stack.stackTagCompound.getInteger("FoodLevel");
            float sat = stack.stackTagCompound.getFloat("FoodSaturation");
            int stamina = stack.stackTagCompound.getInteger("FoodStamina");
            int fatigue = stack.stackTagCompound.getInteger("FoodFatigue");
            affectStats(player, stamina, fatigue);
            player.getFoodStats().addStats(level, sat);
            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        } else return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.stackTagCompound.getInteger("EatTime");
        } else return super.getMaxItemUseDuration(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.stackTagCompound.getBoolean("IsDrink") ? EnumAction.drink : EnumAction.eat;
        } else return EnumAction.none;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.canEat(false)) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }

        return stack;
    }

    //Apply all the relevant information about this meal to the meal stack
    public static ItemStack cook(ItemStack stack, Meal meal) {
        stack.setTagCompound(new NBTTagCompound());
        stack.stackTagCompound.setString("FoodName", meal.unlocalized);
        stack.stackTagCompound.setInteger("FoodLevel", meal.hunger);
        stack.stackTagCompound.setFloat("FoodSaturation", meal.saturation);
        stack.stackTagCompound.setInteger("FoodStamina", meal.stamina);
        stack.stackTagCompound.setInteger("FoodFatigue", meal.fatigue);
        stack.stackTagCompound.setBoolean("IsDrink", meal.isLiquid);
        stack.stackTagCompound.setInteger("EatTime", meal.eatTime);
        stack.setItemDamage(meal.meta);

        return stack;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(MEALPATH + "burnt");
        icons = new IIcon[FoodRegistry.getRecipes().size()];
        for (Recipe recipe : FoodRegistry.getRecipes()) {
            String key = recipe.result.unlocalized;
            if (icons[recipe.result.meta] == null) {
                icons[recipe.result.meta] = register.registerIcon(MEALPATH + StringUtils.replace(key, ".", "_"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creative, List list) {
        for (Recipe recipe : FoodRegistry.getRecipes()) {
            list.add(cook(new ItemStack(item), recipe.getBestMeal()));
        }
    }
}
