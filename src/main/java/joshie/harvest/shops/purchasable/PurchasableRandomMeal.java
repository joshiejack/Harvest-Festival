package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

public class PurchasableRandomMeal extends PurchasableMeal {
    private final int seedAdjustment;
    private long adjustableCost;
    private ItemStack stack;

    public PurchasableRandomMeal(int seedAdjustment) {
        super(0, new ResourceLocation(MODID, "ice_cream"));
        this.seedAdjustment = seedAdjustment;
        this.setStock(10);
    }

    @Override
    public String getPurchaseableID() {
        return "buy[random_" + seedAdjustment + "]";
    }

    @Override
    public long getCost() {
        return adjustableCost;
    }

    @Override
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return HFApi.quests.hasCompleted(Quests.SELL_ORES, player) && canDo(world, player, 1);
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        Random rand = new Random(HFApi.calendar.getDate(world).hashCode() + seedAdjustment);
        List<Recipe> list = Recipe.REGISTRY.getValues();
        stack = null; //Reset the stack
        while(stack == null || !stack.hasTagCompound()) {
            stack = CookingHelper.makeRecipe(list.get(rand.nextInt(list.size())));
        }

        adjustableCost = (long)((double)stack.getTagCompound().getLong(SELL_VALUE) / 1.1);
        adjustableCost = (long)Math.ceil((double)adjustableCost/50) * 50;
        stack.getTagCompound().setLong(SELL_VALUE, 0L);
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public ItemStack getDisplayStack() {
        if (stack == null) {
            stack = CookingHelper.makeRecipe(item);
            stack.getTagCompound().setLong(SELL_VALUE, 0L);
        }

        return stack;
    }
}
