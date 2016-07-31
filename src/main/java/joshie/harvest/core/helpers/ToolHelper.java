package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.base.ItemBaseTool;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.MILKER;
import static joshie.harvest.cooking.items.ItemIngredients.Ingredient.OIL;
import static joshie.harvest.npc.items.ItemNPCTool.NPCTool.BLUE_FEATHER;
import static joshie.harvest.tools.HFTools.EXHAUSTION;
import static joshie.harvest.tools.HFTools.FATIGUE;

public class ToolHelper {
    public static boolean isMilker(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == MILKER;
    }

    public static boolean isBrush(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == BRUSH;
    }

    public static boolean isBlueFeather(ItemStack stack) {
        return HFNPCs.TOOLS.getEnumFromStack(stack) == BLUE_FEATHER;
    }

    public static boolean isEgg(ItemStack heldItem) {
        return heldItem.getItem() == HFAnimals.EGG;
    }

    public static boolean isOil(ItemStack stack) {
        return HFCooking.INGREDIENTS.getEnumFromStack(stack) == OIL;
    }

    public static void levelTool(ItemStack stack) {
        if (stack == null) return;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setDouble("Level", 0D);
        } else {
            double level = stack.getTagCompound().getDouble("Level");
            double increase = ((ItemBaseTool) stack.getItem()).getLevelIncrease(stack);
            double newLevel = Math.min(100D, level + increase);
            stack.getTagCompound().setDouble("Level", newLevel);
        }
    }

    /**
     * Should always be called client and server side
     **/
    public static void performTask(EntityPlayer player, ItemStack stack, float amount) {
        levelTool(stack);
        if (player.capabilities.isCreativeMode) return; //If the player is in creative don't exhaust them
        int level = player.getFoodStats().getFoodLevel();
        player.getFoodStats().addExhaustion(HFTools.EXHAUSTION_AMOUNT * amount); //Add Exhaustion
        if (level > 2 && level <= 6 && !player.isPotionActive(FATIGUE)) {
            player.addPotionEffect(new PotionEffect(FATIGUE, 6000));
        } else if (level <= 2 && !player.isPotionActive(EXHAUSTION)) {
            player.removeActivePotionEffect(FATIGUE);
            player.addPotionEffect(new PotionEffect(EXHAUSTION, 2000));
        } else if (!player.worldObj.isRemote) {
            PotionEffect effect = player.getActivePotionEffect(EXHAUSTION);
            if (effect != null && effect.getDuration() <= 1500) {
                int dimension = player.worldObj.provider.canRespawnHere() ? player.worldObj.provider.getDimension() : 0;
                EntityHelper.teleport(player, dimension, player.getBedLocation(dimension));
                player.trySleep(player.getBedLocation(dimension));
                player.removeActivePotionEffect(FATIGUE);
                player.removeActivePotionEffect(EXHAUSTION);
            }
        }
    }
}
