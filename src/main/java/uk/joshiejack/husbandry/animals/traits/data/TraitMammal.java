package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;

@PenguinLoader("mammal")
public class TraitMammal extends AnimalTraitData {
    private int gestation;//How many days this animal has been pregnant
    private boolean pregnant; //If the animal is pregnant

    public TraitMammal(String name) {
        super(name);
    }

    @Override
    public AnimalTraitData create() {
        return new TraitMammal(getName());
    }

    @Override
    public void onNewDay() {
        if (pregnant) {
            gestation--;
            if (gestation <= 0) {
                pregnant = false;
                giveBirth();
            }
        }
    }

    public boolean impregnate() {
        if (!pregnant && stats.getType().getDaysToBirth() != 0) {
            pregnant = true;
            gestation = stats.getType().getDaysToBirth();
            return true;
        } else return false;
    }

    public boolean isPregnant() {
        return pregnant;
    }

    private void giveBirth() {
        stats.increaseHappiness(100); //Happy to have a child
        int chance = entity.world.rand.nextInt(100);
        int offspring = chance >= 99 ? 3 : chance >= 90 ? 2 : 1;
        for (int i = 0; i < offspring; i++) {
            EntityAgeable ageable = entity.createChild(entity);
            if (ageable != null) {
                ageable.setGrowingAge(-Integer.MAX_VALUE);
                ageable.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                AnimalStats<?> stats = AnimalStats.getStats(ageable);
                if (stats != null) {
                    stats.increaseHappiness(this.stats.getHappiness() / 2);
                }

                entity.world.spawnEntity(ageable);
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Gestation", gestation);
        tag.setBoolean("Pregnant", pregnant);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        gestation = nbt.getInteger("Gestation");
        pregnant = nbt.getBoolean("Pregnant");
    }
}
