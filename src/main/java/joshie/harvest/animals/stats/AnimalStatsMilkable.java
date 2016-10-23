package joshie.harvest.animals.stats;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AnimalStatsMilkable extends AnimalStatsLivestock {
    public AnimalStatsMilkable() {
        this.type = HFApi.animals.getTypeFromString("cow");
    }

    @Override
    public boolean performAction(@Nonnull World world, @Nullable EntityPlayer player, @Nullable ItemStack stack, AnimalAction action) {
        if (action == AnimalAction.TEST_MILK) return !animal.isChild() && canProduce();
        else if (action == AnimalAction.MILK) return milk(world, player);
        else return super.performAction(world, player, stack, action);
    }

    private boolean milk(@Nonnull World world, @Nullable EntityPlayer player) {
        if (player == null) return false;
        if (!world.isRemote && !HFAnimals.OP_ANIMALS) {
            setProduced(getProductsPerDay());
        }

        return true;
    }
}
