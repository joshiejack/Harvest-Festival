package joshie.harvest.cooking.item;

import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemIngredients extends ItemHFEnum<ItemIngredients, Ingredient> {
    public enum Ingredient implements IStringSerializable {
        BUTTER(false), KETCHUP(false), COOKIES(false), EGG_SCRAMBLED(false), SASHIMI(false),
        FLOUR, OIL, RICEBALL, SALT, CHOCOLATE;

        private final boolean isReal;

        Ingredient() {
            isReal = true;
        }

        Ingredient(boolean isReal) {
            this.isReal = isReal;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemIngredients() {
        super(HFTab.COOKING, Ingredient.class);
    }

    @Override
    public boolean shouldDisplayInCreative(Ingredient ingredient) {
        return ingredient.isReal;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    private int getHunger(Ingredient ingredient) {
        switch (ingredient) {
            case RICEBALL: return 1;
            case CHOCOLATE: return 3;
            default: return 0;
        }
    }

    private float getSaturation(Ingredient ingredient) {
        switch (ingredient) {
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
            Ingredient ingredient = getEnumFromStack(stack);
            player.getFoodStats().addStats(getHunger(ingredient), getSaturation(ingredient));
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

            return stack;
        }

        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.canEat(false) && getHunger(getEnumFromStack(stack)) > 0) {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
    }
}
