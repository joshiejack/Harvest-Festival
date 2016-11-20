package joshie.harvest.animals.stats;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.animals.packet.PacketSyncAnimal;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.UUID;

import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

public class AnimalStatsHF implements AnimalStats<NBTTagCompound> {
    protected static final Random rand = new Random();
    private WeakReference<EntityPlayer> owner;
    protected EntityAnimal animal;
    protected IAnimalType type;
    protected UUID o_uuid;

    private int currentLifespan = 0; //How many days this animal has lived for
    protected int healthiness = Byte.MAX_VALUE; //How healthy this animal is, full byte range
    private int stress = Byte.MIN_VALUE; //How stressed this animal is, full byte range
    private int daysNotFed; //How many subsequent days that this animal has not been fed

    private boolean isSick; //Whether the animal is sick or not
    private boolean wasSick; //Whether the animal was previously sick
    private boolean hasDied; //Whether this animal is classed as dead

    //Product based stuff
    private int daysPassed; //How many days have passed so far
    private int productsPerDay = 1; //The maximum number of products this animal can produce a day
    private int producedProducts; //Whether the animal has produced products this day
    private boolean treated; //Whether this animal has had it's treat for today
    private int genericTreats; //Number of generic treats this animal had
    private int typeTreats; //Number of specific treats this animal had
    private boolean wasOutsideInSun;

    public AnimalStatsHF() {
        this.type = HFAnimals.CHICKENS;
    }

    /** Set the animal type
     *  @param type the animal type **/
    public AnimalStatsHF setType(IAnimalType type) {
        this.type = type;
        return this;
    }

    /** Set the animal entity
     *  @param animal the entity **/
    public AnimalStatsHF setEntity(EntityAnimal animal) {
        this.animal = animal;
        return this;
    }

    @Override
    public IAnimalType getType() {
        return type;
    }

    @Override
    public EntityAnimal getAnimal() {
        return animal;
    }

    private int getDeathChance() {
        //If the owner is offline, have a low chance of death
        EntityPlayer owner = getOwner();
        if (owner == null) return Integer.MAX_VALUE;
        //If the animal has not been fed, give it a fix changed of dying
        if (daysNotFed > 0) {
            return Math.max(1, 45 - daysNotFed * 3);
        }

        //Gets the adjusted relationship, 0-35k
        int relationship = HFApi.player.getRelationsForPlayer(owner).getRelationship(EntityHelper.getEntityUUID(animal));
        double chance = (relationship / (double) RelationshipType.ANIMAL.getMaximumRP()) * 200;
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
    public int getProductsPerDay() {
        return productsPerDay;
    }

    @Override
    public void onBihourlyTick() {
        World world = animal.worldObj;
        boolean dayTime = world.isDaytime();
        boolean isRaining = world.isRaining();
        boolean isOutside = world.canBlockSeeSky(new BlockPos(animal));
        if (((isRaining || !dayTime) && isOutside)) {
            stress = (byte) Math.min(Byte.MAX_VALUE, stress + 10);
        } else if (stress > Byte.MIN_VALUE) {
            stress--;
        }

        if (stress > 0) {
            healthiness--;
        }

        boolean isOutsideInSun = !isRaining && isOutside && dayTime;
        EntityPlayer owner = getOwner();
        if (owner != null && isOutsideInSun && wasOutsideInSun) {
            healthiness++;
            affectRelationship(owner, type.getRelationshipBonus(AnimalAction.OUTSIDE));
        }

        //Mark the past value
        wasOutsideInSun = isOutsideInSun;
    }

    protected void preStress() {}
    protected void postStress() {}

    protected void updateStats() {
        treated = false;
    }

    protected void updatePregnancy() {}

    @Override
    public boolean newDay() {
        if (animal != null) {
            //Check if the animal is going to die
            if (hasDied) return false;
            if (currentLifespan > type.getMaxLifespan()) return false;
            if (currentLifespan > type.getMinLifespan()) {
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
            int originalHealth = healthiness;
            healthiness -= daysNotFed;

            preStress();
            if (!HFAnimals.OUTDOOR_HAPPINESS) {
                World world = animal.worldObj;
                boolean isOutside = world.canBlockSeeSky(new BlockPos(animal));
                boolean isRaining = world.isRaining();
                if ((isRaining && isOutside)) {
                    stress = (byte) Math.min(Byte.MAX_VALUE, stress + 50);
                } else if (stress > Byte.MIN_VALUE) {
                    stress--;
                }

                if (stress > 0) {
                    healthiness--;
                }
            }

            postStress();

            //Add health daily for animals that are being looked after
            if (healthiness == originalHealth) {
                healthiness+= 10;
            }

            healthiness = Math.min(127, Math.max(-128, healthiness));
            daysNotFed++;
            daysPassed++;
            updateStats();

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
                    producedProducts = 0;
                    type.refreshProduct(this, animal);
                }
            }

            updatePregnancy();

            sendToEveryone(new PacketSyncAnimal(animal.getEntityId(), this));
            return true;
        } else return false;
    }

    private EntityPlayer getAndCreateOwner() {
        if (owner != null) return owner.get();
        else {
            if (o_uuid != null) {
                owner = new WeakReference<>(EntityHelper.getPlayerFromUUID(o_uuid));
                return owner.get();
            }
        }

        return null;
    }

    @Override
    public EntityPlayer getOwner() {
        EntityPlayer owner = getAndCreateOwner();
        if (owner != null && animal != null) {
            if (animal.worldObj.provider.getDimension() == owner.worldObj.provider.getDimension()) {
                if (animal.getDistanceToEntity(owner) <= 178D) {
                    return owner;
                }
            }
        }

        return null;
    }

    @Override
    public void setOwner(@Nonnull UUID uuid) {
        this.o_uuid = uuid;
    }

    @Override
    public boolean canProduce() {
        return healthiness > 0 && producedProducts < productsPerDay;
    }

    @Override
    public void setProduced(int amount) {
        producedProducts += amount;
        HFApi.animals.syncAnimalStats(animal);
    }

    @Override
    public boolean performTest(AnimalTest test) {
        if (test == AnimalTest.HAS_EATEN) return daysNotFed < 0;
        else if (test == AnimalTest.IS_SICK) return isSick;
        else if (test == AnimalTest.HAD_TREAT) return treated;
        else return false;
    }

    @Override
    public boolean performAction(@Nonnull World world, @Nullable EntityPlayer player, @Nullable ItemStack stack, AnimalAction action) {
        if (action == AnimalAction.FEED) return feed(world, player);
        else if (action == AnimalAction.HEAL) return heal(world, player);
        return (action == AnimalAction.TREAT_SPECIAL || action == AnimalAction.TREAT_GENERIC) && treat(world, player, stack);
    }

    private boolean feed(@Nonnull World world, @Nullable EntityPlayer player) {
        if (daysNotFed >= 0) {
            if (!world.isRemote) {
                daysNotFed = -1;
                affectRelationship(player, type.getRelationshipBonus(AnimalAction.FEED));
                HFApi.animals.syncAnimalStats(animal);
            }

            return true;
        }

        return false;
    }

    private boolean heal(@Nonnull World world, @Nullable EntityPlayer player) {
        if (healthiness < 27) {
            animal.clearActivePotions();
            if (!world.isRemote) {
                healthiness = Byte.MAX_VALUE;
                isSick = false;
                affectRelationship(player, -100);
                HFApi.animals.syncAnimalStats(animal);
            }

            return true;
        } else return false;
    }

    @Override
    public void affectRelationship(EntityPlayer player, int amount) {
        if (player != null && amount != 0) {
            HFApi.player.getRelationsForPlayer(player).affectRelationship(RelationshipType.ANIMAL, EntityHelper.getEntityUUID(animal), amount);
            if (amount < 0) {
                try {
                    ReflectionHelper.findMethod(EntityLivingBase.class, null, new String[]{"playHurtSound"}, DamageSource.class).invoke(animal, DamageSource.starve);
                } catch (IllegalAccessException | InvocationTargetException ex) { ex.printStackTrace(); }
            }
        }
    }

    private void treat(EntityPlayer player, AnimalAction action) {
        treated = true;
        affectRelationship(player, type.getRelationshipBonus(action));
        HFApi.animals.syncAnimalStats(animal);
    }

    private boolean treat(@Nonnull World world, @Nullable EntityPlayer player, @Nullable ItemStack stack) {
        if (stack == null) return false;
        if (!treated) {
            if (HFAnimals.TREATS.getEnumFromStack(stack) == Treat.GENERIC) {
                if (!world.isRemote) {
                    genericTreats++;
                    treat(player, AnimalAction.TREAT_GENERIC);
                }

                return true;
            } else if (HFAnimals.TREATS.getEnumFromStack(stack).getType() == type) {
                if (!world.isRemote) {
                    typeTreats++;
                    treat(player, AnimalAction.TREAT_SPECIAL);
                }

                return true;
            } else {
                if (!world.isRemote) {
                    treat(player, AnimalAction.TREAT_INCORRECT);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Owner")) {
            o_uuid = UUID.fromString(nbt.getString("Owner"));
        }

        currentLifespan = nbt.getShort("CurrentLifespan");
        healthiness = nbt.getByte("Healthiness");
        stress = nbt.getByte("Stress");
        daysNotFed = nbt.getByte("DaysNotFed");
        daysPassed = nbt.getByte("DaysPassed");
        treated = nbt.getBoolean("Treated");
        genericTreats = nbt.getShort("GenericTreats");
        typeTreats = nbt.getShort("TypeTreats");
        wasSick = nbt.getBoolean("WasSick");
        isSick = nbt.getBoolean("IsSick");
        wasOutsideInSun = nbt.getBoolean("WasOutsideInSun");
        if (type.getDaysBetweenProduction() > 0) {
            productsPerDay = nbt.getByte("NumProducts");
            producedProducts = nbt.getByte("ProducedProducts");
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (o_uuid != null) {
            tag.setString("Owner", o_uuid.toString());
        }

        tag.setShort("CurrentLifespan", (short) currentLifespan);
        tag.setByte("Healthiness", (byte) healthiness);
        tag.setByte("Stress", (byte) stress);
        tag.setByte("DaysNotFed", (byte) daysNotFed);
        tag.setByte("DaysPassed", (byte) daysPassed);
        tag.setBoolean("Treated", treated);
        tag.setShort("GenericTreats", (short) genericTreats);
        tag.setShort("TypeTreats", (short) typeTreats);
        tag.setBoolean("WasSick", wasSick);
        tag.setBoolean("IsSick", isSick);
        tag.setBoolean("WasOutsideInSun", wasOutsideInSun);

        if (type.getDaysBetweenProduction() > 0) {
            tag.setByte("NumProducts", (byte) productsPerDay);
            tag.setByte("ProducedProducts", (byte) producedProducts);
        }

        return tag;
    }
}
