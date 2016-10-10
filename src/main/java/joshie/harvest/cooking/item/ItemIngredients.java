package joshie.harvest.cooking.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFoodEnum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Locale;

public class ItemIngredients extends ItemHFFoodEnum<ItemIngredients, Ingredient> implements IShippable {
    public enum Ingredient implements IStringSerializable {
        BUTTER(0L), KETCHUP(0L), COOKIES(0L), EGG_SCRAMBLED(0L), SASHIMI(0L),
        FLOUR(25L), OIL(25L), RICEBALL(50L), SALT(25L), CHOCOLATE(50L),
        CURRY_POWDER(25L), DUMPLING_POWDER(50L), WINE(150L);

        private final long sell;

        Ingredient(long sell) {
            this.sell = sell;
        }

        public long getSellValue() {
            return sell;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemIngredients() {
        super(HFTab.COOKING, Ingredient.class);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return getEnumFromStack(stack).getSellValue();
    }

    @Override
    public boolean shouldDisplayInCreative(Ingredient ingredient) {
        return ingredient.getSellValue() > 0L;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case RICEBALL: return 1;
            case CHOCOLATE: return 3;
            default: return 0;
        }
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case RICEBALL: return 0.25F;
            case CHOCOLATE: return 0.5F;
            default: return 0F;
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (!player.capabilities.isCreativeMode) --stack.stackSize;
            player.getFoodStats().addStats(getHealAmount(stack), getSaturationModifier(stack));
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.canEat(false) && getHealAmount(stack) > 0) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }
}
