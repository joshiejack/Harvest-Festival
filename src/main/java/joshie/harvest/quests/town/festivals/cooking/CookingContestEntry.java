package joshie.harvest.quests.town.festivals.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.tile.TileCookingStand;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

class CookingContestEntry {
    private final ItemStack meal;

    CookingContestEntry(Random rand) {
        meal = HFCooking.MEAL.getRandomMeal(rand);
    }

    CookingContestEntry(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCookingStand) {
            TileCookingStand stand = ((TileCookingStand)tile);
            if (stand.getContents() != null) {
                meal = stand.getContents();
            } else meal = null;
        } else meal = null;
    }

    @Nullable
    public ItemStack getMeal() {
        return meal;
    }

    int getScore() {
        int hunger = ((ItemFood)meal.getItem()).getHealAmount(meal);
        float saturation = ((ItemFood)meal.getItem()).getSaturationModifier(meal);
        long gold = HFApi.shipping.getSellValue(meal);
        return (int)(hunger * saturation + gold);
    }
}
