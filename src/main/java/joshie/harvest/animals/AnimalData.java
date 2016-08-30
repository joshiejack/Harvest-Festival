package joshie.harvest.animals;

import io.netty.buffer.ByteBuf;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.animals.packet.PacketSyncDaysNotFed;
import joshie.harvest.animals.packet.PacketSyncEverything;
import joshie.harvest.animals.packet.PacketSyncHealthiness;
import joshie.harvest.animals.packet.PacketSyncProductsProduced;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

public class AnimalData implements IAnimalData {
    private static final Random rand = new Random();

    private EntityAnimal animal;
    private IRelatable relatable;
    private IAnimalData data;
    private IAnimalType type;

    private EntityPlayer owner;
    private UUID o_uuid;

    private int currentLifespan = 0; //How many days this animal has lived for
    private int healthiness = Byte.MAX_VALUE; //How healthy this animal is, full byte range
    private int cleanliness = 0; //How clean this animal is, full byte range
    private int stress = Byte.MIN_VALUE; //How stressed this animal is, full byte range
    private int daysNotFed; //How many subsequent days that this animal has not been fed

    private boolean isSick; //Whether the animal is sick or not
    private boolean wasSick; //Whether the animal was previously sick
    private boolean hasDied; //Whether this animal is classed as dead

    //Product based stuff
    private int daysPassed; //How many days have passed so far
    private int productsPerDay = 1; //The maximum number of products this animal can produce a day
    private boolean producedProducts; //Whether the animal has produced products this day
    private boolean thrown; //Whether this animal has been thrown or not today, only affects chickens
    private boolean treated; //Whether this animal has had it's treat for today
    private int genericTreats; //Number of generic treats this animal had
    private int typeTreats; //Number of specific treats this animal had

    //Pregnancy Test
    private boolean isPregnant;
    private int daysPregnant;

    public AnimalData(IAnimalTracked animal, IAnimalType type) {
        this.animal = (EntityAnimal) animal;
        this.relatable = animal;
        this.data = animal.getData();
        this.type = type;
    }

    @Override
    public IAnimalType getType() {
        return type;
    }

    @Override
    public boolean hasDied() {
        return hasDied;
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
        int relationship = HFApi.relationships.getRelationship(owner, relatable);
        double chance = (relationship / (double) HFNPCs.MAX_FRIENDSHIP) * 200;
        chance += healthiness;
        if (chance <= 1) {
            chance = 1D;
        }

        return (int) chance;
    }

    public void setDead() {
        this.hasDied = true;
    }

    @Override
    public boolean newDay() {
        if (animal != null) {
            //Check if the animal is going to die
            if (hasDied) return false;
            if (currentLifespan > type.getMaxLifespan() || healthiness <= -120) return false;
            if (currentLifespan > type.getMinLifespan() || healthiness < 0) {
                if (rand.nextInt(getDeathChance()) == 0) {
                    hasDied = true;
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

            World world = animal.worldObj;
            boolean isOutside = world.canBlockSeeSky(new BlockPos(animal));
            boolean isRaining = world.isRaining();
            if ((isRaining && isOutside) || (!isRaining && !isOutside)) {
                stress = (byte) Math.min(Byte.MAX_VALUE, stress + 50);
            } else if (stress > Byte.MIN_VALUE) {
                stress--;
            }

            if (stress > 0) {
                healthiness--;
            }

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
                    animal.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1000000, 0));
                    animal.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 1000000, 0));
                    animal.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1000000, 0));
                }
            } else {
                if (wasSick) {
                    wasSick = false;
                    animal.removePotionEffect(MobEffects.NAUSEA);
                    animal.removePotionEffect(MobEffects.BLINDNESS);
                    animal.removePotionEffect(MobEffects.SLOWNESS);
                }
            }

            //Update the maximum produced products
            if (treated && productsPerDay < 5) {
                int requiredGeneric = type.getGenericTreatCount();
                int requiredType = type.getTypeTreatCount();
                if (genericTreats >= requiredGeneric && requiredType >= typeTreats) {
                    genericTreats -= requiredGeneric;
                    typeTreats -= requiredType;
                    productsPerDay++;
                }
            }

            //Updating grabbing products between animals
            int daysBetween = type.getDaysBetweenProduction();
            if (daysBetween > 0) {
                if (daysPassed >= daysBetween) {
                    daysPassed = 0;
                    producedProducts = false;
                    type.newDay(data, animal);
                }
            }

            //Pregnancy Test!
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

            sendToEveryone(new PacketSyncEverything(animal.getEntityId(), this));
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

    @Override
    public int getProductsPerDay() {
        return productsPerDay;
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
            if (animal.worldObj.provider.getDimension() == owner.worldObj.provider.getDimension()) {
                if (animal.getDistanceToEntity(owner) <= 128) {
                    return owner;
                }
            }
        }

        return null;
    }

    @Override
    public void setOwner(@Nonnull EntityPlayer player) {
        this.owner = player;
        this.o_uuid = UUIDHelper.getPlayerUUID(player);
    }

    @Override
    public boolean canProduce() {
        return healthiness > 0 && !producedProducts && productsPerDay > 0;
    }

    @Override
    public void setProduced() {
        producedProducts = true;
        sendToEveryone(new PacketSyncProductsProduced(animal.getEntityId(), producedProducts));
    }

    @Override
    public void clean(@Nullable EntityPlayer player) {
        if (cleanliness < Byte.MAX_VALUE) {
            cleanliness = (byte) Math.min(Byte.MAX_VALUE, cleanliness + 10);
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
            HFTrackers.getPlayerTracker(player).getRelationships().affectRelationship(player, relatable, amount);
        }
    }

    @Override
    public void feed(@Nullable EntityPlayer player) {
        if (daysNotFed >= 0) {
            daysNotFed = -1;
            affectRelationship(player, 5);
            sendToEveryone(new PacketSyncDaysNotFed(animal.getEntityId(), (byte)daysNotFed));
        }
    }

    @Override
    public boolean heal(@Nullable EntityPlayer player) {
        if (healthiness < 27) {
            healthiness += 100;
            if (healthiness >= 0) {
                isSick = false;
            }

            sendToEveryone(new PacketSyncHealthiness(animal.getEntityId(), (byte)healthiness));
            return true;
        } else return false;
    }

    @Override
    public boolean treat(ItemStack stack, @Nullable EntityPlayer player) {
        if (!treated) {
            treated = HFAnimals.TREATS.getEnumFromStack(stack) == Treat.GENERIC;
            if (treated) {
                genericTreats++;
                affectRelationship(player, 1);
                treated = !HFAnimals.OP_ANIMALS;
                return true;
            } else {
                treated = HFApi.animals.getTypeFromString(HFAnimals.TREATS.getEnumFromStack(stack).name()) == type;
                if (treated) {
                    typeTreats++;
                    affectRelationship(player, 2);
                    treated = !HFAnimals.OP_ANIMALS;
                    return true;
                }
            }
        }



        return false;
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
        for (int i = 0; i < (HFAnimals.MAX_LITTER_SIZE - 1); i++) {
            if (rand.nextDouble() * 100 <= HFAnimals.LITTER_EXTRA_CHANCE) {
                count++;
            }
        }

        //Lay that litter!
        for (int i = 0; i < count; i++) {
            EntityAgeable baby = animal.createChild(animal);
            baby.setGrowingAge(-(24000 * HFAnimals.AGING_TIMER));
            baby.setLocationAndAngles(animal.posX, animal.posY, animal.posZ, 0.0F, 0.0F);
            animal.worldObj.spawnEntityInWorld(baby);
        }
    }

    @Override
    public void setHealthiness(int healthiness) {
        this.healthiness = healthiness;
    }

    @Override
    public void setDaysNotFed(int daysNotFed) {
        this.daysNotFed = daysNotFed;
    }

    @Override
    public void setProductsProduced(boolean producedProducts) {
        this.producedProducts = producedProducts;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(healthiness);
        buf.writeByte(daysNotFed);
        buf.writeByte(productsPerDay);
        buf.writeBoolean(producedProducts);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        healthiness = buf.readByte();
        daysNotFed = buf.readByte();
        productsPerDay = buf.readByte();
        producedProducts = buf.readBoolean();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Owner")) {
            o_uuid = UUID.fromString(nbt.getString("Owner"));
        }

        currentLifespan = nbt.getShort("CurrentLifespan");
        healthiness = nbt.getByte("Healthiness");
        cleanliness = nbt.getByte("Cleanliness");
        stress = nbt.getByte("Stress");
        daysNotFed = nbt.getByte("DaysNotFed");
        daysPassed = nbt.getByte("DaysPassed");
        treated = nbt.getBoolean("Treated");
        genericTreats = nbt.getShort("GenericTreats");
        typeTreats = nbt.getShort("TypeTreats");
        wasSick = nbt.getBoolean("WasSick");
        isSick = nbt.getBoolean("IsSick");
        thrown = nbt.getBoolean("Thrown");
        if (type.getDaysBetweenProduction() > 0) {
            productsPerDay = nbt.getByte("NumProducts");
            producedProducts = nbt.getBoolean("ProducedProducts");
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
        nbt.setByte("Cleanliness", (byte)cleanliness);
        nbt.setByte("Stress", (byte) stress);
        nbt.setByte("DaysNotFed", (byte) daysNotFed);
        nbt.setByte("DaysPassed", (byte) daysPassed);
        nbt.setBoolean("Treated", treated);
        nbt.setShort("GenericTreats", (short) genericTreats);
        nbt.setShort("TypeTreats", (short) typeTreats);
        nbt.setBoolean("WasSick", wasSick);
        nbt.setBoolean("IsSick", isSick);
        nbt.setBoolean("Thrown", thrown);

        if (type.getDaysBetweenProduction() > 0) {
            nbt.setByte("NumProducts", (byte) productsPerDay);
            nbt.setBoolean("ProducedProducts", producedProducts);
        }

        nbt.setBoolean("IsPregnant", isPregnant);
        nbt.setByte("DaysPregnant", (byte) daysPregnant);
    }
}