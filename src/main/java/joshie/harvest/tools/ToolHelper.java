package joshie.harvest.tools;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.cooking.item.ItemIngredients.Ingredient.OIL;
import static joshie.harvest.tools.HFTools.EXHAUSTION;
import static joshie.harvest.tools.HFTools.FATIGUE;

public class ToolHelper {
    public static boolean isBrush(ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == BRUSH;
    }

    //TODO: Reenable in 1.0 when I readd marriage
    @SuppressWarnings("unused")
    public static boolean isBlueFeather(ItemStack stack) {
        return false;
        //return HFNPCs.TOOLS.getEnumFromStack(stack) == BLUE_FEATHER;
    }

    public static boolean isEgg(ItemStack stack) {
        return stack.getItem() == HFAnimals.ANIMAL_PRODUCT && HFAnimals.ANIMAL_PRODUCT.getEnumFromStack(stack) == Sizeable.EGG;
    }

    public static boolean isWool(ItemStack stack) {
        return stack.getItem() == HFAnimals.ANIMAL_PRODUCT && HFAnimals.ANIMAL_PRODUCT.getEnumFromStack(stack) == Sizeable.WOOL;
    }

    public static boolean isOil(ItemStack stack) {
        return HFCooking.INGREDIENTS.getEnumFromStack(stack) == OIL;
    }

    public static boolean isKnife(ItemStack stack) {
        return HFApi.cooking.isKnife(stack);
    }

    public static void levelTool(ItemStack stack) {
        if (stack == null) return;
        if (stack.getItem() instanceof ITiered) {
            ((ITiered)stack.getItem()).levelTool(stack);
        }
    }

    /**
     * Should always be called client and server side
     **/
    public static void performTask(EntityPlayer player, ItemStack stack, float amount) {
        levelTool(stack); //Level up the tool
        if (player.capabilities.isCreativeMode || !HFTools.HF_CONSUME_HUNGER) return; //If the player is in creative don't exhaust them
        consumeHunger(player, amount);
        stack.getSubCompound("Data", true).setInteger("Damage", stack.getSubCompound("Data", true).getInteger("Damage") + 1);
    }

    @SuppressWarnings("ConstantConditions")
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
                if (HFTools.RESTORE_HUNGER_ON_FAINTING) {
                    restoreHunger(player);
                }
            }
        }
    }

    @HFEvents
    @SuppressWarnings("unused")
    public static class RestoreHungerOnSleep {
        public static boolean register() { return HFTools.RESTORE_HUNGER_ON_SLEEP; }

        @SubscribeEvent
        public void onWakeup(PlayerWakeUpEvent event) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.worldObj.getWorldTime() % TICKS_PER_DAY == 0) {
                if (player.isPotionActive(EXHAUSTION)) player.removePotionEffect(EXHAUSTION);
                if (player.isPotionActive(FATIGUE)) player.removePotionEffect(FATIGUE);
                restoreHunger(player);
            }
        }
    }

    private static void restoreHunger(EntityPlayer player) {
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 20, "foodLevel", "field_75127_a");
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 5F, "foodSaturationLevel", "field_75125_b");
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 0, "foodExhaustionLevel", "field_75126_c");
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 0, "foodTimer", "field_75123_d");
    }
}
