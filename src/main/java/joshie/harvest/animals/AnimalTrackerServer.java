package joshie.harvest.animals;

import static joshie.harvest.core.helpers.ServerHelper.markDirty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.config.Animals;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.util.IData;
import joshie.harvest.init.HFBlocks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

//Handles the Data for the crops rather than using TE Data
public class AnimalTrackerServer implements IData {
    private DamageSource natural_causes = new DamageSource("natural").setDamageBypassesArmor();
    private HashSet<IAnimalData> animals = new HashSet();
    private HashSet<ValueLocation> troughs = new HashSet();
    private HashSet<ValueLocation> nests = new HashSet();

    //Gets a Location key
    private ValueLocation getKey(World world, int x, int y, int z) {
        return new ValueLocation(world.provider.dimensionId, x, y, z);
    }

    public static final IAnimalData null_data = new NullAnimalData();

    //Returns a new instanceof this animal
    private IAnimalData getAndCreateData(EntityAnimal animal) {
        if (animal instanceof IAnimalTracked) {
            return ((IAnimalTracked) animal).getData();
        }

        return null_data;
    }

    //Returns the entityplayer that is the owner of this animal, or the closest to it
    public EntityPlayer getOwner(EntityAnimal animal) {
        return getAndCreateData(animal).getOwner();
    }

    //Sets the owner of this animal
    public void setOwner(EntityPlayer player, EntityAnimal animal) {
        getAndCreateData(animal).setOwner(player);
    }

    //Kills an animal
    private void kill(EntityAnimal animal) {
        if (animal != null) {
            animal.attackEntityFrom(natural_causes, 1000F);
        }
    }
    
    public void onJoinWorld(EntityAnimal animal) {
        animals.add(getAndCreateData(animal));
    }

    //Called when an animal dies
    public void onDeath(EntityAnimal animal) {
        animals.remove(getAndCreateData(animal));
    }

    //Loops through all the animal, and 'ticks' them for their new day
    public boolean newDay() {
        //Grab all the troughs in the world and set the animals around them to be fed
        for (ValueLocation trough : troughs) {
            if (trough.getValue() <= 0) continue;
            World world = DimensionManager.getWorld(trough.dimension);
            ArrayList<IAnimalTracked> animals = (ArrayList<IAnimalTracked>) world.getEntitiesWithinAABB(IAnimalTracked.class, HFBlocks.cookware.getCollisionBoundingBoxFromPool(world, trough.x, trough.y, trough.z).expand(16D, 16D, 16D));
            for (IAnimalTracked animal : animals) {
                if (trough.getValue() <= 0) break;
                if (AnimalHelper.eatsGrass(animal)) {
                    setFed((EntityAnimal) animal);
                    trough.decr();
                }
            }
        }

        //Hatch nest if they have passed 4 days of nesting
        for (ValueLocation nest : nests) {
            if (nest.getValue() <= 0) continue;
            if (nest.getValue() < Animals.CHICKEN_TIMER) {
                nest.incr();
                continue;
            }

            if (nest.getValue() >= Animals.CHICKEN_TIMER) {
                World world = DimensionManager.getWorld(nest.dimension);
                EntityChicken baby = new EntityChicken(world).createChild(new EntityChicken(world));
                baby.setGrowingAge(-(12000 * Animals.AGING_TIMER));
                baby.setLocationAndAngles(nest.x, nest.y, nest.z, 0.0F, 0.0F);
                baby.worldObj.spawnEntityInWorld(baby);
                nest.reset();
            }
        }

        Iterator<IAnimalData> iter = animals.iterator();
        while (iter.hasNext()) {
            IAnimalData data = iter.next();
            if (!data.newDay()) { //If the new day wasn't successful, remove the animal from your memory
                iter.remove();
                kill(data.getAnimal());
            }
        }

        return true;
    }

    public void addTrough(World world, int x, int y, int z) {
        troughs.add(getKey(world, x, y, z));
        markDirty();
    }

    public boolean addFood(World world, int x, int y, int z, int amount) {
        ValueLocation value = getKey(world, x, y, z);
        for (ValueLocation v : troughs) {
            if (v == value) {
                for (int i = 0; i < amount; i++) {
                    v.incr();
                }

                return true;
            }
        }

        return false;
    }

    public void removeTrough(World world, int x, int y, int z) {
        troughs.remove(getKey(world, x, y, z));
        markDirty();
    }

    public void addNest(World world, int x, int y, int z) {
        nests.add(getKey(world, x, y, z));
        markDirty();
    }

    public boolean addEgg(World world, int x, int y, int z) {
        ValueLocation value = getKey(world, x, y, z);
        for (ValueLocation v : nests) {
            if (v == value) {
                if (v.getValue() <= 0) {
                    v.incr();
                    return true;
                } else return false;
            }
        }

        return false;
    }

    public boolean addFodder(World world, int x, int y, int z) {
        ValueLocation value = getKey(world, x, y, z);
        for (ValueLocation v : troughs) {
            if (v == value) {
                v.incr();
                return true;
            }
        }

        return false;
    }

    public void removeNest(World world, int x, int y, int z) {
        nests.remove(getKey(world, x, y, z));
        markDirty();
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

    //Boosts an animals health
    public boolean heal(EntityAnimal animal) {
        return getAndCreateData(animal).heal();
    }

    //Treats an animal, gives it a relationship boost if you haven't had one today
    public void treat(ItemStack stack, EntityPlayer player, EntityAnimal animal) {
        getAndCreateData(animal).treat(stack, player);
    }

    public boolean impregnate(EntityAnimal animal) {
        return getAndCreateData(animal).impregnate();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList trough = nbt.getTagList("Troughs", 10);
        for (int i = 0; i < trough.tagCount(); i++) {
            NBTTagCompound tag = trough.getCompoundTagAt(i);
            ValueLocation location = new ValueLocation();
            location.readFromNBT(tag);
            troughs.add(location);
        }

        NBTTagList nest = nbt.getTagList("Nests", 10);
        for (int i = 0; i < nest.tagCount(); i++) {
            NBTTagCompound tag = nest.getCompoundTagAt(i);
            ValueLocation location = new ValueLocation();
            location.readFromNBT(tag);
            nests.add(location);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        //Animal Troughs
        NBTTagList trough = new NBTTagList();
        for (WorldLocation location : troughs) {
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            trough.appendTag(tag);
        }

        nbt.setTag("Troughs", trough);

        //Chicken Nests
        NBTTagList nest = new NBTTagList();
        for (WorldLocation location : nests) {
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            nest.appendTag(tag);
        }

        nbt.setTag("Nests", nest);
    }
}
