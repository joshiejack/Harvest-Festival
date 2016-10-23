package joshie.harvest.animals.stats;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static joshie.harvest.api.animals.IAnimalHandler.ANIMAL_STATS_CAPABILITY;
import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

public class AnimalStatsLivestock extends AnimalStatsHF {
    private int cleanliness = 0; //How clean this animal is, full byte range

    //Pregnancy Test
    private boolean isPregnant;
    private int daysPregnant;
    public AnimalStatsLivestock() {
        this.type = HFApi.animals.getTypeFromString("cow");
    }

    @Override
    protected void preStress() {
        cleanliness--;
    }

    @Override
    protected void postStress() {
        if (cleanliness < 0) {
            healthiness += cleanliness;
        } else if (cleanliness >= 0) {
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
    public boolean performAction(@Nonnull World world, @Nullable EntityPlayer player, @Nullable ItemStack stack, AnimalAction action) {
        if (action == AnimalAction.CLEAN) return clean(world, player);
        else if (action == AnimalAction.IMPREGNATE) return impregnate(player);
        else return super.performAction(world, player, stack, action);
    }

    private boolean clean(@Nonnull World world, @Nullable EntityPlayer player) {
        if (cleanliness < Byte.MAX_VALUE) {
            if (!world.isRemote) {
                cleanliness = (byte) Math.min(Byte.MAX_VALUE, cleanliness + 10);
                if (cleanliness >= Byte.MAX_VALUE) {
                    affectRelationship(player, 30);
                }
            }
        }

        return true;
    }

    private boolean impregnate(EntityPlayer player) {
        if (animal.getAge() < 0) return false;
        if (isPregnant) return false;
        daysPregnant = 0;
        isPregnant = true;
        affectRelationship(player, 200);
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
            baby.getCapability(ANIMAL_STATS_CAPABILITY, EnumFacing.DOWN).setOwner(o_uuid);
            int parent = HFTrackers.<PlayerTrackerServer>getPlayerTracker(animal.worldObj, o_uuid).getRelationships().getRelationship(EntityHelper.getEntityUUID(animal));
            HFTrackers.getPlayerTracker(animal.worldObj, o_uuid).getRelationships().copyRelationship(getOwner(), parent, EntityHelper.getEntityUUID(baby), 50D);
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
