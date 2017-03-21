package joshie.harvest.cooking.item;

import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFFoodEnum;
import joshie.harvest.core.util.interfaces.ISellable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import java.util.Locale;

public class ItemIngredients extends ItemHFFoodEnum<ItemIngredients, Ingredient> {
    public enum Ingredient implements IStringSerializable, ISellable {
        CURRY_POWDER(50L, 15L), DUMPLING_POWDER, WINE(200L, 50L), UNUSED1, UNUSED2,
        FLOUR(50L, 15L), OIL(50L, 15L), RICEBALL(100L, 25L), SALT(25L, 5L), CHOCOLATE(100L, 25L);

        private final long cost;
        private final long sell;

        Ingredient(long cost, long sell) {
            this.cost = cost;
            this.sell = sell;
        }

        Ingredient() {
            this(0L, 0L);
        }

        public long getCost() {
            return cost;
        }

        public boolean isDrink() {
            return this == WINE || this == OIL;
        }

        @Override
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
    protected ItemStack getCreativeStack(Ingredient ingredient) {
        return ingredient.getSellValue() > 0L ? getStackFromEnum(ingredient) : null;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return getEnumFromStack(stack).isDrink() ? EnumAction.DRINK : EnumAction.EAT;
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case CURRY_POWDER:
            case DUMPLING_POWDER:
            case FLOUR:
            case RICEBALL:
                return 1;
            case WINE:
                return 2;
            case CHOCOLATE: return 3;
            default: return 0;
        }
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        switch (getEnumFromStack(stack)) {
            case RICEBALL: return 0.25F;
            case CHOCOLATE: return 0.5F;
            case CURRY_POWDER:
            case DUMPLING_POWDER:
                return 0.2F;
            case FLOUR:
                return 0.6F;
            case WINE:
                return 0.8F;
            default: return 0F;
        }
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
