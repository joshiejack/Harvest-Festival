package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.husbandry.network.PacketCleaned;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import net.minecraft.nbt.NBTTagCompound;

@PenguinLoader("cleanable")
public class TraitCleanable extends AnimalTraitData {
    private int cleanliness;
    private boolean cleaned;

    public TraitCleanable(String name) {
        super(name);
    }

    @Override
    public AnimalTraitData create() {
        return new TraitCleanable(getName());
    }

    @Override
    public void onNewDay() {
        setCleaned(false);
        cleanliness = MathsHelper.constrainToRangeInt(cleanliness - 10, -100, 100);
        if (cleanliness <= 0) {
            stats.decreaseHappiness(1); //We dirty, so we no happy
        }
    }

    public boolean isClean() {
        return cleaned;
    }

    public boolean clean() {
        if (!cleaned) {
            cleanliness++;
            if (cleanliness == 100) {
                setCleaned(true);
            }
        }

        return cleaned;
    }

    public void setCleaned(boolean cleaned) {
        if (entity.world.isRemote) this.cleaned = cleaned;
        else {
            this.cleaned = cleaned;
            if (cleaned) {
                stats.increaseHappiness(30);
            }

            PenguinNetwork.sendToNearby(entity, new PacketCleaned(entity.getEntityId(), cleaned));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Cleanliness", cleanliness);
        tag.setBoolean("Cleaned", cleaned);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        cleanliness = nbt.getInteger("Cleanliness");
        cleaned = nbt.getBoolean("Cleaned");
    }
}
