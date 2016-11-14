package joshie.harvest.animals.stats;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AnimalStatsPoultry extends AnimalStatsHF {
    private boolean thrown; //Whether this animal has been thrown or not today, only affects chickens

    public AnimalStatsPoultry() {
        this.type = HFAnimals.CHICKENS;
    }

    @Override
    public IAnimalType getType() {
        return type;
    }

    @Override
    protected void updateStats() {
        super.updateStats();
        thrown = false;
    }

    @Override
    public boolean performTest(AnimalTest test) {
        if (test == AnimalTest.CAN_CARRY) return true;
        else return super.performTest(test);
    }

    @Override
    public boolean performAction(@Nonnull World world, @Nullable EntityPlayer player, @Nullable ItemStack stack, AnimalAction action) {
        if (action == AnimalAction.DISMOUNT) return dismount(player);
        else return super.performAction(world, player, stack, action);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean dismount(@Nullable EntityPlayer player) {
        if (!thrown) {
            thrown = true;
            animal.setInLove(player);
            return true;
        }

        return false;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        thrown = nbt.getBoolean("Thrown");
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        tag.setBoolean("Thrown", thrown);
        return tag;
    }
}
