package joshie.harvest.tools;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.animals.item.ItemAnimalTool.Tool.BRUSH;
import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;
import static joshie.harvest.tools.HFTools.*;

public class ToolHelper {
    private static final DamageSource EXHAUSTED = new DamageSource("exhausted") {
        @Override
        @Nonnull
        public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
            String s = "harvestfestival.death.attack." + this.damageType;
            return new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName());
        }
    }.setDamageBypassesArmor().setDamageIsAbsolute();

    public static boolean isBrush(@Nonnull ItemStack stack) {
        return HFAnimals.TOOLS.getEnumFromStack(stack) == BRUSH;
    }

    //TODO: Reenable in 1.0 when I readd marriage
    @SuppressWarnings("unused")
    public static boolean isBlueFeather(@Nonnull ItemStack stack) {
        return false;
        //return HFNPCs.TOOLS.getEnumFromStack(stack) == BLUE_FEATHER;
    }

    public static boolean isEgg(@Nonnull ItemStack stack) {
        return stack.getItem() == HFAnimals.ANIMAL_PRODUCT && HFAnimals.ANIMAL_PRODUCT.getEnumFromStack(stack) == Sizeable.EGG;
    }

    public static boolean isWool(@Nonnull ItemStack stack) {
        return stack.getItem() == HFAnimals.ANIMAL_PRODUCT && HFAnimals.ANIMAL_PRODUCT.getEnumFromStack(stack) == Sizeable.WOOL;
    }

    public static void levelTool(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) return;
        if (stack.getItem() instanceof ITiered) {
            ((ITiered) stack.getItem()).levelTool(stack);
        }
    }

    /**
     * Should always be called client and server side
     **/
    public static void performTask(EntityPlayer player, @Nonnull ItemStack stack, @Nonnull ItemTool tool) {
        levelTool(stack); //Level up the tool
        if (player.capabilities.isCreativeMode || !HFTools.HF_CONSUME_HUNGER)
            return; //If the player is in creative don't exhaust them
        consumeHunger(player, tool.getExhaustionRate(stack));
        if (tool.canBeDamaged()) {
            int max = tool.getMaxDamage(stack);
            int current = tool.getDamage(stack);
            if (current + 1 >= max) player.renderBrokenItemStack(stack);
            stack.damageItem(1, player);
        }
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
            if (effect != null && ((level == 0 && effect.getDuration() <= 1990) || (ENABLE_EARLY_FAINTING && effect.getDuration() <= 1500))) {
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 7));
                if (!player.world.isRemote && ENABLE_FAINTING) {
                    if (ENABLE_DEATH_FAINTING) player.attackEntityFrom(EXHAUSTED, 1000F);
                    else {
                        int dimension = player.world.provider.canRespawnHere() ? player.world.provider.getDimension() : 0;
                        BlockPos spawn = player.getBedLocation(dimension) != null ? player.getBedLocation(dimension) : DimensionManager.getWorld(dimension).provider.getRandomizedSpawnPoint();
                        EntityHelper.teleport(player, dimension, spawn);
                        player.trySleep(spawn);
                        if (ENABLE_FAINTING_SLEEP) { //Force instant sleep
                            player.sleepTimer = 100;
                        }
                    }
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
        public static boolean register() {
            return HFTools.RESTORE_HUNGER_ON_SLEEP;
        }

        @SubscribeEvent
        public void onWakeup(PlayerWakeUpEvent event) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.world.getWorldTime() % TICKS_PER_DAY == 0) {
                if (player.isPotionActive(EXHAUSTION)) player.removePotionEffect(EXHAUSTION);
                if (player.isPotionActive(FATIGUE)) player.removePotionEffect(FATIGUE);
                restoreHunger(player);
            }
        }
    }

    public static void restoreHunger(EntityPlayer player) {
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 20, "foodLevel", "field_75127_a");
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 5F, "foodSaturationLevel", "field_75125_b");
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 0, "foodExhaustionLevel", "field_75126_c");
        ReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), 0, "foodTimer", "field_75123_d");
    }

    @SuppressWarnings("deprecation")
    public static void collectDrops(World world, BlockPos pos, IBlockState state, EntityPlayer player, NonNullList<ItemStack> drops) {
        Block block = state.getBlock();
        List<ItemStack> blockDrops = new ArrayList<>();
        if (block.canSilkHarvest(world, pos, state, player)) {
            try {
                Method method = ReflectionHelper.findMethod(Block.class, null, new String[]{ "getSilkTouchDrop", "func_180643_i" }, IBlockState.class);
                ItemStack stack = (ItemStack) method.invoke(block, state);
                if (!stack.isEmpty()) {
                    blockDrops.add(stack);
                    ForgeEventFactory.fireBlockHarvesting(blockDrops, world, pos, state, 0, 1F, true, player);
                    drops.addAll(blockDrops); //Add all the drops to our list
                }
            } catch (IllegalAccessException | InvocationTargetException e) {/**/}
        } else {
            blockDrops = block.getDrops(world, pos, state, 0);
            ForgeEventFactory.fireBlockHarvesting(blockDrops, world, pos, state, 0, 1F, false, player);
            drops.addAll(blockDrops); //Add all the drops to our list
        }
    }

    @Nonnull
    public static ItemStack getStackFromBlockState(IBlockState state) {
        ItemStack stack = ItemStack.EMPTY;
        try {
            Method method = ReflectionHelper.findMethod(Block.class, null, new String[] { "createStackedBlock", "func_180643_i" } , IBlockState.class);
            stack = (ItemStack) method.invoke(state.getBlock(), state);
        } catch (IllegalAccessException | InvocationTargetException | ReflectionHelper.UnableToFindMethodException ignored) {
        }
        return stack;
    }
}