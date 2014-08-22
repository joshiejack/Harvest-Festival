package harvestmoon.entities;

import harvestmoon.helpers.EntityHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldSavedData;

//Handles the Data for the crops rather than using TE Data
public class AnimalTrackerServer extends WorldSavedData {
    private static DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    public static final String DATA_NAME = "HM-Tracker-Animals";

    //Key is the animal, Value is the Player
    private static HashMap<UUID, AnimalData> animalData = new HashMap();

    public AnimalTrackerServer(String string) {
        super(string);
    }

    //Returns a new instanceof this animal
    private AnimalData getAndCreateData(EntityAnimal animal) {
        AnimalData data = animalData.get(animal.getPersistentID());
        if (data == null) {
            data = new AnimalData(animal);
            animalData.put(animal.getPersistentID(), data);
        }

        markDirty();
        return data;
    }

    //Returns the entityplayer that is the owner of this animal, or the closest to it
    public EntityPlayer getOwner(EntityAnimal animal) {
        return getAndCreateData(animal).getOwner(animal);
    }

    //Sets the owner of this animal
    public void setOwner(EntityPlayer player, EntityAnimal animal) {
        getAndCreateData(animal).setOwner(player);
    }

    //Kills an animal
    private void kill(UUID uuid) {
        EntityAnimal animal = EntityHelper.getAnimalFromUUID(uuid);
        if (animal != null) {
            animal.attackEntityFrom(natural_causes, 1000F);
        }
    }

    //Called when an animal dies
    public void onDeath(EntityAnimal animal) {
        animalData.remove(animal.getPersistentID());
        markDirty();
    }

    //Loops through all the animal, and 'ticks' them for their new day
    public boolean newDay() {
        Iterator<Map.Entry<UUID, AnimalData>> iter = animalData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<UUID, AnimalData> entry = iter.next();
            AnimalData data = entry.getValue();
            if (!data.newDay()) { //If the new day wasn't successful, remove the animal from your memory
                iter.remove();
                kill(entry.getKey());
            }
        }

        markDirty();
        return true;
    }

    //Whether this animal can produce any products or not
    public boolean canProduceProduct(EntityAnimal animal) {
        return getAndCreateData(animal).canProduce();
    }

    //This animal has now produced a product, increase it's value
    public void setProducedProduct(EntityAnimal animal) {
        getAndCreateData(animal).setProduced();
    }

    //Returns true if the animal has been cleaned
    public boolean setCleaned(EntityAnimal animal) {
        return getAndCreateData(animal).setCleaned();
    }
    
    //Only chickens can be thrown, Returns true if they haven't been thrown today
    public boolean setThrown(EntityChicken chicken) {
        return getAndCreateData(chicken).setThrown();
    }

    //This animal has now been fed
    public boolean setFed(EntityAnimal animal) {
        return getAndCreateData(animal).setFed();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList animals = nbt.getTagList("AnimalData", 10);
        for (int i = 0; i < animals.tagCount(); i++) {
            NBTTagCompound tag = animals.getCompoundTagAt(i);
            UUID id = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
            AnimalData data = new AnimalData();
            data.readFromNBT(tag);
            animalData.put(id, data);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList animals = new NBTTagList();
        for (Map.Entry<UUID, AnimalData> entry : animalData.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setLong("UUIDMost", entry.getKey().getMostSignificantBits());
            tag.setLong("UUIDLeast", entry.getKey().getLeastSignificantBits());
            entry.getValue().writeToNBT(tag);
            animals.appendTag(tag);
        }

        nbt.setTag("AnimalData", animals);
    }
}
