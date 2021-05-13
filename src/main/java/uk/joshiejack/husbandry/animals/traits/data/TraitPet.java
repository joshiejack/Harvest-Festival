package uk.joshiejack.husbandry.animals.traits.data;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;

@PenguinLoader("pet")
public class TraitPet extends AnimalTraitData {
    private int skill;

    public TraitPet(String name) {
        super(name);
    }

    @Override
    public AnimalTraitData create() {
        return new TraitPet(getName());
    }

    @Override
    public void onNewDay() { }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Skill", skill);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        skill = nbt.getInteger("Skill");
    }
}
