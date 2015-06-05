package joshie.harvest.animals;

import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

import java.util.Random;
import java.util.UUID;

import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.core.config.Animals;
import joshie.harvest.core.helpers.RelationsHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.network.PacketSyncCanProduce;
import joshie.harvest.core.util.IData;
import joshie.harvest.items.ItemTreat;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AnimalData implements IData, IAnimalData {
    private static final Random rand = new Random();

    private EntityAnimal animal;
    private EntityPlayer owner;
    private IAnimalTracked tracking;

    private UUID a_uuid;
    private UUID o_uuid;
    private int dimension; //The dimension this animal was last in

    private int currentLifespan = 0; //How many days this animal has lived for
    private int healthiness = 127; //How healthy this animal is, full byte range
    private int cleanliness = 0; //How clean this animal is, full byte range
    private int daysNotFed; //How many subsequent days that this animal has not been fed

    private boolean sickCheck; //Whether to check if the animal is sick
    private boolean isSick; //Whether the animal is sick or not

    //Product based stuff
    private int daysPassed; //How many days have passed so far
    private int maxProductsPerDay = 1; //The maximum number of products this animal can produce a day
    private int numProductsProduced = 0; //The number of products this animal has produced today (resets each day)
    private boolean thrown; //Whether this animal has been thrown or not today, only affects chickens
    private boolean treated; //Whether this animal has had it's treat for today

    //Pregnancy Test
    private boolean isPregnant;
    private int daysPregnant;

    public AnimalData(IAnimalTracked animal) {
        this.tracking = animal;
        this.animal = (EntityAnimal) animal;
        this.a_uuid = UUIDHelper.getEntityUUID(this.animal);
    }

    /** May return null **/
    public EntityPlayer getAndCreateOwner() {
        if (o_uuid != null) {
            if (owner == null) {
                owner = joshie.harvest.core.helpers.generic.EntityHelper.getPlayerFromUUID(o_uuid);
            }

            return owner;
        } else return null;
    }

    private int getDeathChance() {
        //If the animal has not been fed, give it a fix changed of dying
        if (daysNotFed > 0) {
            return Math.max(1, 45 - daysNotFed * 3);
        }

        //Gets the adjusted relationship, 0-65k
        int relationship = RelationsHelper.getRelationshipValue(animal, getOwner());
        double chance = (relationship / (double) RelationsHelper.ADJUSTED_MAX) * 200;
        chance += healthiness;
        if (chance <= 1) {
            chance = 1D;
        }

        return (int) chance;
    }

    public boolean newDay() {
        if (animal != null) {
            //Stage 1, Check if the Animal is going to die
            if (currentLifespan > tracking.getType().getMaxLifespan()) return false;
            if (currentLifespan > tracking.getType().getMinLifespan() || isSick) {
                if (rand.nextInt(getDeathChance()) == 0) {
                    return false;
                }
            }

            //Stage 1.5 Chance for animal to get sick if healthiness below 100
            if (!isSick) {
                if (healthiness < 100) {
                    if (rand.nextInt(Math.max(1, healthiness)) == 0) {
                        sickCheck = true;
                        isSick = true;
                    }
                }
            }

            //Stage 2, Do the basic increasing and resetting of counters
            thrown = false;
            treated = false;
            currentLifespan++;
            daysNotFed++;
            daysPassed++;
            healthiness -= daysNotFed;
            if (cleanliness < 0) {
                healthiness += cleanliness;
            }

            if (sickCheck) {
                if (isSick) {
                    animal.addPotionEffect(new PotionEffect(Potion.confusion.id, 1000000, 0));
                    animal.addPotionEffect(new PotionEffect(Potion.blindness.id, 1000000, 0));
                    animal.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1000000, 0));
                } else {
                    animal.removePotionEffect(Potion.confusion.id);
                    animal.removePotionEffect(Potion.blindness.id);
                    animal.removePotionEffect(Potion.moveSlowdown.id);

                    sickCheck = false;
                }
            }

            if (cleanliness > 0) {
                cleanliness = 0;
            } else if (cleanliness <= 0) {
                cleanliness--;
            }

            //Stage 3, Reset the products produced per day
            if (tracking.getType().getDaysBetweenProduction() > 0) {
                if (daysPassed >= tracking.getType().getDaysBetweenProduction()) {
                    daysPassed = 0;
                    numProductsProduced = 0;
                    tracking.getType().newDay(animal);
                }

                //Sync Whether this animal can produce, before we increase daysnotfed
                sendToEveryone(new PacketSyncCanProduce(animal.getEntityId(), false, canProduce()));
            }

            //Stage 4 Pregnancy time!
            if (isPregnant) {
                daysPregnant++;

                if (daysPregnant >= Animals.PREGNANCY_TIMER) {
                    isPregnant = false;
                    daysPregnant = 0;
                    giveBirth();
                }
            }

            //Stage 5 Growth time!
            if (animal.isChild()) {
                animal.addGrowth(1200);
            }

            return true;
        } else return false;
    }

    @Override
    public EntityAnimal getAnimal() {
        return animal;
    }

    @Override
    public EntityPlayer getOwner() {
        EntityPlayer owner = getAndCreateOwner();
        if (owner != null) {
            if (animal.worldObj.provider.dimensionId == owner.worldObj.provider.dimensionId) {
                if (animal.getDistanceToEntity(owner) <= 128) {
                    return owner;
                } else return null;
            } else return null;
        }

        return animal.worldObj.getClosestPlayerToEntity(animal, 128);
    }

    @Override
    public void setOwner(EntityPlayer player) {
        this.owner = player;
        this.o_uuid = UUIDHelper.getPlayerUUID(player);
    }

    public int getDimension() {
        if (animal != null) {
            return animal.worldObj.provider.dimensionId;
        } else return dimension;
    }

    //Animals can produce products, if they are healthy, have been fed, and aren't over their daily limit
    @Override
    public boolean canProduce() {
        return healthiness > 0 && daysNotFed <= 0 && numProductsProduced < maxProductsPerDay;
    }

    //Increase the amount of products this animal has produced for the day
    @Override
    public void setProduced() {
        numProductsProduced++;
        //Increase the amount produced, then resync the data with the client
        sendToEveryone(new PacketSyncCanProduce(animal.getEntityId(), false, canProduce()));
    }

    @Override
    public boolean setCleaned() {
        if (cleanliness < Byte.MAX_VALUE) {
            cleanliness += 10;
            return cleanliness >= Byte.MAX_VALUE;
        } else return false;
    }

    @Override
    public boolean setThrown() {
        if (!thrown) {
            thrown = true;
            return true;
        } else return false;
    }

    //Sets this animal as having been fed, if it's already been fed, this will return false
    @Override
    public boolean setFed() {
        if (daysNotFed >= 0) {
            daysNotFed = -1;
            return true;
        } else return false;
    }

    //Returns true if the animal was healed
    @Override
    public boolean heal() {
        if (healthiness < 27) {
            healthiness += 100;
            if (healthiness >= 0) {
                isSick = false;
            }

            return true;
        } else return false;
    }

    //If the animal hasn't been treated yet today, and it's the right treat, then reward some points
    @Override
    public void treat(ItemStack stack, EntityPlayer player) {
        if (!treated) {
            IAnimalType type = ItemTreat.getTreatTypeFromMeta(stack.getItemDamage());
            if (type == tracking.getType()) {
                treated = true;
                RelationsHelper.affectRelations(player, animal, 1000);
            }
        }
    }

    @Override
    public boolean impregnate() {
        if (animal.getAge() < 0) return false;
        if (isPregnant) return false;
        daysPregnant = 0;
        isPregnant = true;
        return true;
    }

    public void giveBirth() {
        int count = 1;
        //Chance for litters up to 5
        for (int i = 0; i < (Animals.MAX_LITTER_SIZE - 1); i++) {
            if (rand.nextDouble() * 100 <= Animals.LITTER_EXTRA_CHANCE) {
                count++;
            }
        }

        //Lay that litter!
        for (int i = 0; i < count; i++) {
            EntityAgeable baby = animal.createChild(animal);
            baby.setGrowingAge(-(24000 * Animals.AGING_TIMER));
            baby.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, 0.0F, 0.0F);
            animal.worldObj.spawnEntityInWorld(baby);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Owner-UUIDMost")) {
            o_uuid = new UUID(nbt.getLong("Owner-UUIDMost"), nbt.getLong("Owner-UUIDLeast"));
        }

        a_uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
        currentLifespan = nbt.getShort("CurrentLifespan");
        healthiness = nbt.getByte("Healthiness");
        cleanliness = nbt.getByte("Cleanliness");
        daysNotFed = nbt.getByte("DaysNotFed");
        daysPassed = nbt.getByte("DaysPassed");
        treated = nbt.getBoolean("Treated");
        sickCheck = nbt.getBoolean("CheckIfSick");
        isSick = nbt.getBoolean("IsSick");
        dimension = nbt.getInteger("Dimension");
        if (tracking.getType() == AnimalRegistry.chicken) thrown = nbt.getBoolean("Thrown");
        if (tracking.getType().getDaysBetweenProduction() > 0) {
            maxProductsPerDay = nbt.getByte("NumProducts");
            numProductsProduced = nbt.getByte("ProductsProduced");
        }

        isPregnant = nbt.getBoolean("IsPregnant");
        daysPregnant = nbt.getInteger("DaysPregnant");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (o_uuid != null) {
            nbt.setLong("Owner-UUIDMost", o_uuid.getMostSignificantBits());
            nbt.setLong("Owner-UUIDLeast", o_uuid.getLeastSignificantBits());
        }

        nbt.setShort("CurrentLifespan", (short) currentLifespan);
        nbt.setByte("Healthiness", (byte) healthiness);
        nbt.setByte("Cleanliness", (byte) cleanliness);
        nbt.setByte("DaysNotFed", (byte) daysNotFed);
        nbt.setByte("DaysPassed", (byte) daysPassed);
        nbt.setBoolean("Treated", treated);
        nbt.setBoolean("CheckIfSick", sickCheck);
        nbt.setBoolean("IsSick", isSick);

        if (animal != null) {
            nbt.setInteger("Dimension", animal.worldObj.provider.dimensionId);
        }

        if (tracking.getType() == AnimalRegistry.chicken) nbt.setBoolean("Thrown", thrown);
        if (tracking.getType().getDaysBetweenProduction() > 0) {
            nbt.setByte("NumProducts", (byte) maxProductsPerDay);
            nbt.setByte("ProductsProduced", (byte) numProductsProduced);
        }

        nbt.setBoolean("IsPregnant", isPregnant);
        nbt.setInteger("DaysPregnant", daysPregnant);
    }
}
