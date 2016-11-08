package joshie.harvest.npc.gift;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;

public class GiftsCandice extends Gifts {
    private boolean isDrinkWithMilk(Meal meal) {
        return meal == Meal.MILK_HOT || meal == Meal.MILK_STRAWBERRY || meal == Meal.LATTE_MIX || meal == Meal.LATTE_FRUIT || meal == Meal.LATTE_VEGETABLE;
    }

    @Override
    public Quality getQuality(ItemStack stack) {
        IGiftRegistry registry = HFApi.npc.getGifts();
        if (stack.getItem() == Items.MILK_BUCKET || ToolHelper.isMilk(stack) ||
                (stack.getItem() == HFCooking.MEAL && isDrinkWithMilk(HFCooking.MEAL.getEnumFromStack(stack))))return Quality.AWESOME;
        else if (registry.isGiftType(stack, ANIMAL)) return Quality.GOOD;
        else if (stack.getItem() == Items.APPLE || stack.getItem() == Items.GOLDEN_APPLE || stack.getItem() == Items.MELON) return Quality.TERRIBLE;
        else if (registry.isGiftType(stack, FRUIT)) return Quality.BAD;
        else if (registry.isGiftType(stack, SWEET)) return Quality.DISLIKE;
        return super.getQuality(stack);
    }
}
