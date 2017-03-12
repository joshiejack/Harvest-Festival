package joshie.harvest.animals.stats;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class AnimalStatsLivestock extends AnimalStatsHF {
    private int cleanliness = 0; //How clean this animal is, full byte range

    //Pregnancy Test
    private boolean isPregnant;
    private int daysPregnant;
    public AnimalStatsLivestock() {
        this.type = HFAnimals.SHEEP;
    }

    @Override
    protected void preStress() {
        cleanliness--;
    }

    @Override
    protected void postStress() {
        if (cleanliness >= 0) {
            cleanliness = 0;
        }
    }

    @Override
    protected void updateStats() {
        super.updateStats();
        if (isPregnant) {
            daysPregnant++;
        }
    }

    @Override
    protected void updatePregnancy() {
        if (isPregnant) {
            daysPregnant++;
            if (daysPregnant >= HFAnimals.PREGNANCY_TIMER) {
                isPregnant = false;
                daysPregnant = 0;
                giveBirth();
            }
        }

        //Children should grow!
        if (animal.isChild()) {
            animal.addGrowth(1200);
        }
    }

    @Override
    public boolean performTest(AnimalTest test) {
        if (test == AnimalTest.CAN_CLEAN) return true;
        else if (test == AnimalTest.IS_CLEAN) return cleanliness == Byte.MAX_VALUE;
        else return super.performTest(test);
    }

    @Override
    public boolean performAction(@Nonnull World world, @Nullable ItemStack stack, AnimalAction action) {
        if (action == AnimalAction.CLEAN) return clean(world);
        else if (action == AnimalAction.IMPREGNATE) return impregnate();
        else return super.performAction(world, stack, action);
    }

    private boolean clean(@Nonnull World world) {
        if (cleanliness < Byte.MAX_VALUE) {
            if (!world.isRemote) {
                cleanliness = (byte) Math.min(Byte.MAX_VALUE, cleanliness + 20);
                if (cleanliness >= Byte.MAX_VALUE) {
                    affectHappiness(type.getRelationshipBonus(AnimalAction.CLEAN));
                    HFApi.animals.syncAnimalStats(animal);
                }
            }

            return true;
        }

        return false;
    }

    private boolean impregnate() {
        if (animal.getAge() < 0) return false;
        if (isPregnant) return false;
        daysPregnant = 0;
        isPregnant = true;
        affectHappiness(type.getRelationshipBonus(AnimalAction.IMPREGNATE));
        HFApi.animals.syncAnimalStats(animal);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private void giveBirth() {
        int count = 1;
        //Chance for litters up to 5
        for (int i = 0; i < (HFAnimals.MAX_LITTER_SIZE - 1); i++) {
            if (rand.nextDouble() * 100 <= HFAnimals.LITTER_EXTRA_CHANCE) {
                count++;
            }
        }

        //Lay that litter!
        for (int i = 0; i < count; i++) {
            EntityAgeable baby = animal.createChild(animal);
            baby.setGrowingAge(-(int)(TICKS_PER_DAY * HFAnimals.AGING_TIMER));
            baby.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, 0.0F, 0.0F);
            AnimalStats stats = EntityHelper.getStats(baby);
            if (stats != null) {
                stats.copyHappiness(getOwner(), getHappiness(), 50D);
            }

            animal.worldObj.spawnEntityInWorld(baby);
        }
    }



    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        cleanliness = nbt.getByte("Cleanliness");
        isPregnant = nbt.getBoolean("IsPregnant");
        daysPregnant = nbt.getByte("DaysPregnant");
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        tag.setByte("Cleanliness", (byte)cleanliness);
        tag.setBoolean("IsPregnant", isPregnant);
        tag.setByte("DaysPregnant", (byte) daysPregnant);
        return tag;
    }
}
