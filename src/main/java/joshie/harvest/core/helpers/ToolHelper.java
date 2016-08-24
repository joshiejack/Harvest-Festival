package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.items.ItemUtensil.Utensil;
import joshie.harvest.core.base.ItemTool;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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

    public static boolean isEgg(ItemStack stack) {
        return stack.getItem() == HFAnimals.EGG;
    }

    public static boolean isOil(ItemStack stack) {
        return HFCooking.INGREDIENTS.getEnumFromStack(stack) == OIL;
    }

    public static boolean isKnife(ItemStack stack) {
        return stack.getItem() == HFCooking.UTENSILS && HFCooking.UTENSILS.getEnumFromStack(stack) == Utensil.KNIFE;
    }

    public static void levelTool(ItemStack stack) {
        if (stack == null) return;
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setDouble("Level", 0D);
        } else {
            double level = stack.getTagCompound().getDouble("Level");
            double increase = ((ItemTool) stack.getItem()).getLevelIncrease(stack);
            double newLevel = Math.min(100D, level + increase);
            stack.getTagCompound().setDouble("Level", newLevel);
        }
    }

    /**
     * Should always be called client and server side
     **/
    public static void performTask(EntityPlayer player, ItemStack stack, float amount) {
        if (player.capabilities.isCreativeMode || !HFTools.HF_CONSUME_HUNGER) return; //If the player is in creative don't exhaust them
        consumeHunger(player, amount);
    }

    public static void consumeHunger(EntityPlayer player, float amount) {
        if (player == null) return; //No null players allowed
        int level = player.getFoodStats().getFoodLevel();
        if (amount > 0F) player.getFoodStats().addExhaustion(HFTools.EXHAUSTION_AMOUNT * amount); //Add Exhaustion
        if (level > 2 && level <= 6) {
            player.removePotionEffect(EXHAUSTION); //Don't ever have fatigue/exhaustion at same time
            player.addPotionEffect(new PotionEffect(FATIGUE, 6000));
        } else if (level <= 2 && !player.isPotionActive(EXHAUSTION)) {
            player.removePotionEffect(FATIGUE); //Don't ever have fatigue/exhaustion at same time
            player.addPotionEffect(new PotionEffect(EXHAUSTION, 2000));
        } else {
            PotionEffect effect = player.getActivePotionEffect(EXHAUSTION);
            if (level == 0 || effect != null && effect.getDuration() <= 1500) {
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 7));
                if (!player.worldObj.isRemote) {
                    int dimension = player.worldObj.provider.canRespawnHere() ? player.worldObj.provider.getDimension() : 0;
                    BlockPos spawn = player.getBedLocation(dimension) != null ? player.getBedLocation(dimension) : DimensionManager.getWorld(dimension).provider.getRandomizedSpawnPoint();
                    EntityHelper.teleport(player, dimension, spawn);
                    player.trySleep(spawn);
                }

                //Remove all effects
                player.removePotionEffect(FATIGUE);
                player.removePotionEffect(EXHAUSTION);
                if (HFTools.RESTORE_HUNGER_ON_SLEEP) {
                    ReflectionHelper.setPrivateValue(EntityPlayer.class, player, new FoodStats(), "foodStats");
                }
            }
        }
    }
}
