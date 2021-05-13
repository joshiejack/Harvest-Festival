package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.AnimalTrait;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AnimalTraitData extends AnimalTrait implements INBTSerializable<NBTTagCompound> {
    protected AnimalStats<?> stats;
    protected EntityAgeable entity;

    public AnimalTraitData(String name) {
        super(name);
    }

    public AnimalTraitData setAnimal(AnimalStats<?> stats, EntityAgeable entity) {
        this.stats = stats;
        this.entity = entity;
        return this;
    }

    public abstract void onNewDay();

    public void onBihourlyTick() {}

    public abstract AnimalTraitData create();
}
