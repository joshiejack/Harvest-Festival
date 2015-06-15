package joshie.harvest.animals;

import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

import java.util.Random;
import java.util.UUID;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.config.Animals;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.network.PacketSyncCanProduce;
import joshie.harvest.items.ItemTreat;
import joshie.harvest.relations.RelationshipHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AnimalData implements IAnimalData {
    private static final Random rand = new Random();

    private EntityAnimal animal;
    private IRelatable relatable;
    private IAnimalData data;
    private IAnimalType type;

    private EntityPlayer owner;
    private UUID o_uuid;

    private short currentLifespan = 0; //How many days this animal has lived for
    private byte healthiness = 127; //How healthy this animal is, full byte range
    private byte cleanliness = 0; //How clean this animal is, full byte range
    private byte daysNotFed; //How many subsequent days that this animal has not been fed

    private boolean isSick; //Whether the animal is sick or not
    private boolean wasSick; //Whether the animal was previously sick

    //Product based stuff
    private byte daysPassed; //How many days have passed so far
    private byte maxProductsPerDay = 1; //The maximum number of products this animal can produce a day
    private byte numProductsProduced = 0; //The number of products this animal has produced today (resets each day)
    private boolean thrown; //Whether this animal has been thrown or not today, only affects chickens
    private boolean treated; //Whether this animal has had it's treat for today

    //Pregnancy Test
    private boolean isPregnant;
    private byte daysPregnant;

    public AnimalData(IAnimalTracked animal) {
        this.animal = (EntityAnimal) animal;
        this.relatable = animal;
        this.data = animal.getData();
        this.type = animal.getType();
    }

    private int getDeathChance() {
        //If the owner is offline, have a low chance of death
        EntityPlayer owner = getOwner();
        if (owner == null) return Integer.MAX_VALUE;
        //If the animal has not been fed, give it a fix changed of dying
        if (daysNotFed > 0) {
            return Math.max(1, 45 - daysNotFed * 3);
        }

        //Gets the adjusted relationship, 0-65k
        int relationship = HFApi.RELATIONS.getAdjustedRelationshipValue(owner, relatable);
        double chance = (relationship / (double) RelationshipHelper.ADJUSTED_MAX) * 200;
        chance += healthiness;
        if (chance <= 1) {
            chance = 1D;
        }

        return (int) chance;
    }

    @Override
    public boolean newDay() {
        if (animal != null) {
            //Check if the animal is going to die
            if (currentLifespan > type.getMaxLifespan() || healthiness <= -120) return false;
            if (currentLifespan > type.getMinLifespan() || healthiness < 0) {
                if (rand.nextInt(getDeathChance()) == 0) {
                    return false;
                }
            }

            //If the animal is not sick, check the healthiness
            if (!isSick) {
                if (healthiness < 0) {
                    isSick = true; //Make the animal sick
                }
            }

            //Reset everything and increase where appropriate
            currentLifespan++;
            healthiness -= daysNotFed;
            cleanliness--;

            if (cleanliness < 0) {
                healthiness += cleanliness;
            } else if (cleanliness >= 0) {
                cleanliness = 0;
            }

            daysNotFed++;
            daysPassed++;
            thrown = false;
            treated = false;

            if (isPregnant) {
                daysPregnant++;
            }

            //Updating potion effects on the animal
            if (isSick) {
                if (!wasSick) {
                    wasSick = true;
                    animal.addPotionEffect(new PotionEffect(Potion.confusion.id, 1000000, 0));
                    animal.addPotionEffect(new PotionEffect(Potion.blindness.id, 1000000, 0));
                    animal.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1000000, 0));
                }
            } else {
                if (wasSick) {
                    wasSick = false;
                    animal.removePotionEffect(Potion.confusion.id);
                    animal.removePotionEffect(Potion.blindness.id);
                    animal.removePotionEffect(Potion.moveSlowdown.id);
                }
            }

            //Updating grabbing products between animals
            int daysBetween = type.getDaysBetweenProduction();
            if (daysBetween > 0) {
                if (daysPassed >= daysBetween) {
                    daysPassed = 0;
                    numProductsProduced = 0;
                    type.newDay(data, animal);
                }

                sendToEveryone(new PacketSyncCanProduce(animal.getEntityId(), false, canProduce()));
            }

            //Pregnancy Test!
            if (isPregnant) {
                daysPregnant++;
                if (daysPregnant >= Animals.PREGNANCY_TIMER) {
                    isPregnant = false;
                    daysPregnant = 0;
                    giveBirth();
                }
            }

            //Children should grow!
            if (animal.isChild()) {
                animal.addGrowth(1200);
            }

            return true;
        } else return false;
    }

    @Override
    public boolean isHungry() {
        return daysNotFed >= 0;
    }

    @Override
    public EntityAnimal getAnimal() {
        return animal;
    }

    private EntityPlayer getAndCreateOwner() {
        if (o_uuid != null) {
            if (owner == null) {
                owner = EntityHelper.getPlayerFromUUID(o_uuid);
            }

            return owner;
        }

        return null;
    }

    @Override
    public EntityPlayer getOwner() {
        EntityPlayer owner = getAndCreateOwner();
        if (owner != null) {
            if (animal.worldObj.provider.dimensionId == owner.worldObj.provider.dimensionId) {
                if (animal.getDistanceToEntity(owner) <= 128) {
                    return owner;
                }
            }
        }

        return null;
    }

    @Override
    public void setOwner(EntityPlayer player) {
        this.owner = player;
        this.o_uuid = UUIDHelper.getPlayerUUID(player);
    }

    @Override
    public boolean canProduce() {
        return healthiness > 0 && daysNotFed <= 0 && numProductsProduced < maxProductsPerDay;
    }

    @Override
    public void setProduced() {
        numProductsProduced++;
        sendToEveryone(new PacketSyncCanProduce(animal.getEntityId(), false, canProduce()));
    }

    @Override
    public void clean(EntityPlayer player) {
        if (cleanliness < Byte.MAX_VALUE) {
            cleanliness += 10;
            if (cleanliness >= Byte.MAX_VALUE) {
                affectRelationship(player, 25);
            }
        }
    }

    @Override
    public void dismount(EntityPlayer player) {
        if (!thrown) {
            thrown = true;
            affectRelationship(player, 100);
        }
    }

    private void affectRelationship(EntityPlayer player, int amount) {
        if (player != null) {
            DataHelper.getPlayerTracker(player).getRelationships().affectRelationship(relatable, amount);
        }
    }

    @Override
    public void feed(EntityPlayer player) {
        if (daysNotFed >= 0) {
            daysNotFed = -1;
            affectRelationship(player, 5);
        }
    }

    @Override
    public boolean heal(EntityPlayer player) {
        if (healthiness < 27) {
            healthiness += 100;
            if (healthiness >= 0) {
                isSick = false;
            }

            return true;
        } else return false;
    }

    @Override
    public void treat(ItemStack stack, EntityPlayer player) {
        if (!treated) {
            IAnimalType type = ItemTreat.getTreatTypeFromStack(stack);
            if (type == this.type) {
                treated = true;
                affectRelationship(player, 1000);
            }
        }
    }

    @Override
    public boolean impregnate(EntityPlayer player) {
        if (animal.getAge() < 0) return false;
        if (isPregnant) return false;
        daysPregnant = 0;
        isPregnant = true;
        affectRelationship(player, 200);
        return true;
    }

    private void giveBirth() {
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
        if (nbt.hasKey("Owner")) {
            o_uuid = UUID.fromString(nbt.getString("Owner"));
        }

        currentLifespan = nbt.getShort("CurrentLifespan");
        healthiness = nbt.getByte("Healthiness");
        cleanliness = nbt.getByte("Cleanliness");
        daysNotFed = nbt.getByte("DaysNotFed");
        daysPassed = nbt.getByte("DaysPassed");
        treated = nbt.getBoolean("Treated");
        wasSick = nbt.getBoolean("WasSick");
        isSick = nbt.getBoolean("IsSick");
        thrown = nbt.getBoolean("Thrown");
        if (type.getDaysBetweenProduction() > 0) {
            maxProductsPerDay = nbt.getByte("NumProducts");
            numProductsProduced = nbt.getByte("ProductsProduced");
        }

        isPregnant = nbt.getBoolean("IsPregnant");
        daysPregnant = nbt.getByte("DaysPregnant");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (o_uuid != null) {
            nbt.setString("Owner", o_uuid.toString());
        }

        nbt.setShort("CurrentLifespan", (short) currentLifespan);
        nbt.setByte("Healthiness", (byte) healthiness);
        nbt.setByte("Cleanliness", (byte) cleanliness);
        nbt.setByte("DaysNotFed", (byte) daysNotFed);
        nbt.setByte("DaysPassed", (byte) daysPassed);
        nbt.setBoolean("Treated", treated);
        nbt.setBoolean("WasSick", wasSick);
        nbt.setBoolean("IsSick", isSick);
        nbt.setBoolean("Thrown", thrown);

        if (type.getDaysBetweenProduction() > 0) {
            nbt.setByte("NumProducts", (byte) maxProductsPerDay);
            nbt.setByte("ProductsProduced", (byte) numProductsProduced);
        }

        nbt.setBoolean("IsPregnant", isPregnant);
        nbt.setByte("DaysPregnant", daysPregnant);
    }
}
