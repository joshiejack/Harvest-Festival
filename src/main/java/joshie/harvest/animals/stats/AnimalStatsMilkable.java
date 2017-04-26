package joshie.harvest.animals.stats;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalTest;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AnimalStatsMilkable extends AnimalStatsLivestock {
    public AnimalStatsMilkable() {
        this.type = HFAnimals.COWS;
    }

    @Override
    public boolean performTest(AnimalTest test) {
        if (test == AnimalTest.CAN_MILK) return !animal.isChild() && canProduce();
        else return super.performTest(test);
    }

    @Override
    public boolean performAction(@Nonnull World world, @Nonnull ItemStack stack, AnimalAction action) {
        if (action == AnimalAction.CLAIM_PRODUCT) return milk(world);
        else return super.performAction(world, stack, action);
    }

    private boolean milk(@Nonnull World world) {
        if (!world.isRemote) {
            setProduced(getProductsPerDay());
        }

        return true;
    }
}
