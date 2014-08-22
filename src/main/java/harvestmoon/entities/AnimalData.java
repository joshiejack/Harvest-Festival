package harvestmoon.entities;

import static harvestmoon.entities.AnimalData.AnimalType.CAT;
import static harvestmoon.entities.AnimalData.AnimalType.CHICKEN;
import static harvestmoon.entities.AnimalData.AnimalType.COW;
import static harvestmoon.entities.AnimalData.AnimalType.DOG;
import static harvestmoon.entities.AnimalData.AnimalType.HORSE;
import static harvestmoon.entities.AnimalData.AnimalType.OTHER;
import static harvestmoon.entities.AnimalData.AnimalType.PIG;
import static harvestmoon.entities.AnimalData.AnimalType.SHEEP;
import static harvestmoon.helpers.EntityHelper.getAnimalFromUUID;
import static harvestmoon.helpers.ItemHelper.spawnByEntity;
import static harvestmoon.helpers.SizeableHelper.getSizeable;
import static harvestmoon.network.PacketHandler.sendToEveryone;
import harvestmoon.HarvestMoon;
import harvestmoon.calendar.CalendarServer;
import harvestmoon.lib.SizeableMeta;
import harvestmoon.lib.SizeableMeta.Size;
import harvestmoon.network.PacketSyncCanProduce;
import harvestmoon.util.IData;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class AnimalData implements IData {
    private static final Random rand = new Random();

    //Enum of animal types
    protected static enum AnimalType {
        COW(12* (CalendarServer.DAYS_PER_SEASON * 4), 20 * (CalendarServer.DAYS_PER_SEASON * 4), 1), 
        SHEEP(8 * (CalendarServer.DAYS_PER_SEASON * 4), 12 * (CalendarServer.DAYS_PER_SEASON * 4), 7), 
        CHICKEN(3 * (CalendarServer.DAYS_PER_SEASON * 4), 10 * (CalendarServer.DAYS_PER_SEASON * 4), 1), 
        HORSE(20 * (CalendarServer.DAYS_PER_SEASON * 4), 30 * (CalendarServer.DAYS_PER_SEASON * 4), 0), 
        PIG(6 * (CalendarServer.DAYS_PER_SEASON * 4), 10 * (CalendarServer.DAYS_PER_SEASON * 4), 0), 
        CAT(10 * (CalendarServer.DAYS_PER_SEASON * 4), 20 * (CalendarServer.DAYS_PER_SEASON * 4), 0), 
        DOG(9 * (CalendarServer.DAYS_PER_SEASON * 4), 16 * (CalendarServer.DAYS_PER_SEASON * 4), 0), 
        OTHER(5 * (CalendarServer.DAYS_PER_SEASON * 4), 10 * (CalendarServer.DAYS_PER_SEASON * 4), 0);

        private final int min;
        private final int max;
        private final int days;

        private AnimalType(int min, int max, int days) {
            this.min = min;
            this.max = max;
            this.days = days;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getDays() {
            return days;
        }
    }

    private UUID animal; //The id of this animal
    private UUID owner; //The owner of this animal

    private AnimalType type;
    private int currentLifespan = 0; //How many days this animal has lived for
    private int healthiness = 127; //How healthy this animal is, full byte range
    private int cleanliness = 0; //How clean this animal is, full byte range
    private int daysNotFed; //How many subsequent days that this animal has not been fed

    //Product based stuff
    private int daysPassed; //How many days have passed so far
    private int maxProductsPerDay = 1; //The maximum number of products this animal can produce a day
    private int numProductsProduced = 0; //The number of products this animal has produced today (resets each day)
    private boolean thrown; //Whether this animal has been thrown or not today, only affects chickens

    public AnimalData() {}

    public AnimalData(EntityAnimal animal) {
        this.animal = animal.getPersistentID();
        if (animal instanceof EntityChicken) {
            this.type = CHICKEN;
        } else if (animal instanceof EntitySheep) {
            this.type = SHEEP;
        } else if (animal instanceof EntityCow) {
            this.type = COW;
        } else if (animal instanceof EntityPig) {
            this.type = PIG;
        } else if (animal instanceof EntityHorse) {
            this.type = HORSE;
        } else if (animal instanceof EntityOcelot) {
            this.type = CAT;
        } else if (animal instanceof EntityWolf) {
            this.type = DOG;
        } else {
            this.type = OTHER;
        }
    }

    private int getDeathChance() {
        //TODO: RECALCULATE DEATH CHANCE
        int base = currentLifespan > type.getMin() ? 100 : 10;
        if (daysNotFed > 0) {
            base /= daysNotFed;
        }

        int relationship = HarvestMoon.handler.getServer().getPlayerData(MinecraftServer.getServer().getEntityWorld(), owner).getRelationship(getAnimalFromUUID(animal));
        double chance = 1D + (relationship / Short.MAX_VALUE) * 100D;
        return (int) Math.max(1, (base * chance));
    }

    public boolean newDay() {
        EntityAnimal entity = getAnimalFromUUID(animal);
        if (entity != null) {
            //Stage 1, Check if the Animal is going to die
            if (currentLifespan > type.getMax() || healthiness < -100) return false;
            if (currentLifespan > type.getMin() || healthiness < 0) {
                if (rand.nextInt(getDeathChance()) == 0) {
                    return false;
                }
            }

            //Stage 2, Do the basic increasing and resetting of counters
            thrown = false;
            currentLifespan++;
            daysNotFed++;
            daysPassed++;
            healthiness -= daysNotFed;
            if (cleanliness < 0) {
                healthiness += cleanliness;
            }

            if (cleanliness > 0) {
                cleanliness = 0;
            } else if (cleanliness <= 0) {
                cleanliness--;
            }

            //Stage 3, Reset the products produced per day
            if (type.getDays() > 0) {
                if (daysPassed >= type.getDays()) {
                    daysPassed = 0;
                    numProductsProduced = 0;

                    //Stage 4, if the animal is a sheep, make it eat grass
                    if (type == SHEEP) {
                        if (entity != null) {
                            entity.eatGrassBonus();
                        }
                    } else if (type == CHICKEN) { //Or if it's a chicken, make it lay an egg
                        EntityPlayer player = HarvestMoon.handler.getServer().getAnimalTracker().getOwner(entity);
                        if (entity != null && player != null) {
                            ItemStack egg = getSizeable(player, entity, SizeableMeta.EGG, Size.LARGE);
                            entity.playSound("mob.chicken.plop", 1.0F, (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
                            spawnByEntity(entity, egg);
                        }
                    }
                }

                //Sync Whether this animal can produce, before we increase daysnotfed
                sendToEveryone(new PacketSyncCanProduce(entity.getEntityId(), false, canProduce()));
            }
        }

        return true;
    }

    //Returns this owner of this animal, can be null
    public EntityPlayer getOwner(EntityAnimal animal) {
        if (owner != null) {
            for (EntityPlayer player : (ArrayList<EntityPlayer>) animal.worldObj.playerEntities) {
                if (player.getPersistentID() == owner) {
                    if (animal.getDistanceToEntity(player) < 128) {
                        return player;
                    } else return null;
                }
            }
        }

        return animal.worldObj.getClosestPlayerToEntity(animal, 128);
    }

    //Sets the owner of this animal
    public void setOwner(EntityPlayer player) {
        owner = player.getPersistentID();
    }

    //Animals can produce products, if they are healthy, have been fed, and aren't over their daily limit
    public boolean canProduce() {
        return healthiness > 0 && daysNotFed <= 0 && numProductsProduced < maxProductsPerDay;
    }

    //Increase the amount of products this animal has produced for the day
    public void setProduced() {
        numProductsProduced++;
        //Increase the amount produced, then resync the data with the client
        EntityAnimal entity = getAnimalFromUUID(animal);
        sendToEveryone(new PacketSyncCanProduce(entity.getEntityId(), false, canProduce()));
    }

    public boolean setCleaned() {
        if (cleanliness < Byte.MAX_VALUE) {
            cleanliness += 10;
            return cleanliness >= Byte.MAX_VALUE;
        } else return false;
    }

    public boolean setThrown() {
        if (!thrown) {
            thrown = true;
            return true;
        } else return false;
    }

    //Sets this animal as having been fed, if it's already been fed, this will return false
    public boolean setFed() {
        if (daysNotFed >= 0) {
            daysNotFed = -1;
            return true;
        } else return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Owner-UUIDMost")) {
            owner = new UUID(nbt.getLong("Owner-UUIDMost"), nbt.getLong("Owner-UUIDLeast"));
        }

        animal = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
        type = AnimalType.values()[nbt.getByte("AnimalType")];
        currentLifespan = nbt.getShort("CurrentLifespan");
        healthiness = nbt.getByte("Healthiness");
        cleanliness = nbt.getByte("Cleanliness");
        daysNotFed = nbt.getByte("DaysNotFed");
        daysPassed = nbt.getByte("DaysPassed");
        if (type == CHICKEN) thrown = nbt.getBoolean("Thrown");
        if (type.days > 0) {
            maxProductsPerDay = nbt.getByte("NumProducts");
            numProductsProduced = nbt.getByte("ProductsProduced");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (owner != null) {
            nbt.setLong("Owner-UUIDMost", owner.getMostSignificantBits());
            nbt.setLong("Owner-UUIDLeast", owner.getLeastSignificantBits());
        }

        nbt.setByte("AnimalType", (byte) type.ordinal());
        nbt.setShort("CurrentLifespan", (short) currentLifespan);
        nbt.setByte("Healthiness", (byte) healthiness);
        nbt.setByte("Cleanliness", (byte) cleanliness);
        nbt.setByte("DaysNotFed", (byte) daysNotFed);
        nbt.setByte("DaysPassed", (byte) daysPassed);
        if (type == CHICKEN) nbt.setBoolean("Thrown", thrown);
        if (type.days > 0) {
            nbt.setByte("NumProducts", (byte) maxProductsPerDay);
            nbt.setByte("ProductsProduced", (byte) numProductsProduced);
        }
    }
}
