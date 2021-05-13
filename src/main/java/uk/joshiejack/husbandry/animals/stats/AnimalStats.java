package uk.joshiejack.husbandry.animals.stats;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.husbandry.HusbandryConfig;
import uk.joshiejack.husbandry.animals.AnimalType;
import uk.joshiejack.husbandry.animals.traits.action.AnimalTraitAction;
import uk.joshiejack.husbandry.animals.traits.data.AnimalTraitData;
import uk.joshiejack.husbandry.animals.traits.data.AnimalTraitProduct;
import uk.joshiejack.husbandry.entity.ai.EntityAITimerBase;
import uk.joshiejack.husbandry.network.PacketHeartParticle;
import uk.joshiejack.husbandry.network.PacketSendData;
import uk.joshiejack.husbandry.scripting.controller.AnimalTownController;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.forge.CapabilityHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static uk.joshiejack.husbandry.animals.stats.CapabilityStatsHandler.ANIMAL_STATS_CAPABILITY;

public class AnimalStats<E extends EntityAgeable> implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
    private static final int MAX_RELATIONSHIP = 30000;
    private static final int MIN_MEDIUM = 2;
    private static final int MIN_LARGE = 5;
    private static final DamageSource OLD_AGE = new DamageSource("oldage");
    protected final E entity;
    protected final AnimalType type;
    private int town;
    private int age; //Current animal age
    private int happiness; //Current animal happiness
    private int happinessDivisor = 5; //Maximum happiness for this animal currently, increased by treats
    private int hunger; //How many days the animal has been without food
    private boolean hasProduct = true; ///If the animal has a product
    private int genericTreatsGiven;
    private int typeTreatsGiven;
    private int childhood;//How many days of childhood so far
    protected boolean loved; //If the animal has been loved today
    private boolean eaten; //If the animal has eaten today
    private boolean treated; //If the animal has been treated
    private boolean special; //If the animal is special
    private boolean wasOutsideInSun; //If the animal was outside last time
    private final Map<String, AnimalTraitData> data;
    private final AnimalTraitProduct products;

    public AnimalStats(E entity, AnimalType type) {
        this.entity = entity;
        this.type = type;
        this.data = Maps.newHashMap();
        if (this.type != null)
            this.type.getDataTraits().forEach(trait -> data.put(trait.getName(), trait.create().setAnimal(this, entity)));
        this.products = (AnimalTraitProduct) this.data.values().stream().filter(t-> t instanceof AnimalTraitProduct).findFirst().orElse(null);
        if (entity != null) {
            this.town = AnimalTownController.getTown(entity);
        }
    }

    public AnimalType getType() {
        return type;
    }

    public int getTown() {
        return town;
    }

    public void onBihourlyTick() {
        World world = entity.world;
        BlockPos pos = new BlockPos(entity);
        boolean dayTime = world.isDaytime();
        boolean isRaining = world.isRaining();
        boolean isOutside = world.canBlockSeeSky(pos);
        boolean isOutsideInSun = !isRaining && isOutside && dayTime && !world.canSnowAt(pos, true);
        if (isOutsideInSun && wasOutsideInSun) {
            increaseHappiness(2);
        }

        //Mark the past value
        wasOutsideInSun = isOutsideInSun;
        data.values().forEach(AnimalTraitData::onBihourlyTick);
    }

    public void resetProduct() {
        entity.eatGrassBonus();
        hasProduct = true;
    }

    public void onNewDay() {
        int chance = MathsHelper.constrainToRangeInt(HusbandryConfig.deathChance, 1, Short.MAX_VALUE);
        if (age >= type.getMaxAge() || (age >= type.getMinAge() && entity.world.rand.nextInt(chance) == 0)) {
            entity.attackEntityFrom(OLD_AGE, Integer.MAX_VALUE);
        }

        data.values().forEach(AnimalTraitData::onNewDay);

        if (!eaten) {
            hunger++;
            decreaseHappiness(1);
        }

        if (happinessDivisor > 1 && genericTreatsGiven >= type.getGenericTreats() && typeTreatsGiven >= type.getTypeTreats()) {
            genericTreatsGiven -= type.getGenericTreats();
            typeTreatsGiven -= type.getTypeTreats();
            happinessDivisor--;
        }

        loved = false;
        eaten = false;
        treated = false;

        if (entity.isChild()) {
            childhood++;

            if (childhood >= type.getDaysToMaturity()) {
                entity.setGrowingAge(0); //Grow up!
            }
        }

        //Force the animals to execute important ai tasks?
        entity.tasks.taskEntries.stream()
                .filter(ai -> ai.action instanceof EntityAITimerBase)
                .forEach(ai -> ((EntityAITimerBase)ai.action).resetRunTimer());

        PenguinNetwork.sendToNearby(entity, new PacketSendData(entity.getEntityId(), this));
    }

    public int getHappiness() {
        return happiness;
    }

    public void decreaseHappiness(int happiness) {
        this.happiness = MathsHelper.constrainToRangeInt(this.happiness - happiness, 0, MAX_RELATIONSHIP / happinessDivisor);
        if (!entity.world.isRemote) {
            PenguinNetwork.sendToNearby(entity, new PacketHeartParticle(entity.getEntityId(), false));
        }
    }

    public void increaseHappiness(int happiness) {
        this.happiness = MathsHelper.constrainToRangeInt(this.happiness + happiness, 0, MAX_RELATIONSHIP / happinessDivisor);
        if (!entity.world.isRemote) {
            PenguinNetwork.sendToNearby(entity, new PacketHeartParticle(entity.getEntityId(), true));
        }
    }

    /** @return true if the event should be canceled */
    public boolean onRightClick(EntityPlayer player, EnumHand hand) {
        for (AnimalTraitAction action: type.getActionTraits()) {
            if (action.onRightClick(this, entity, player, hand)) return true;
        }

        return false;
    }

    public void feed() {
        hunger = 0;
        eaten = true;
    }

    public boolean treat(boolean generic) {
        if (!treated) {
            if (generic) {
                genericTreatsGiven++;
            } else typeTreatsGiven++;

            increaseHappiness(generic ? 250 : 100);
            treated = true;
            return true;
        }

        return false;
    }

    public boolean hasBeenTreated() {
        return treated;
    }

    public boolean hasBeenLoved() {
        return loved;
    }

    public boolean setLoved() {
        this.loved = true;
        increaseHappiness(100);
        return this.loved;
    }

    public boolean canProduceProduct() {
        return hasProduct && !entity.isChild();
    }

    public void setProduced(int amount) {
        this.products.setProduced(amount);
        this.hasProduct = false;
    }

    public boolean hasEaten() {
        return eaten;
    }

    public ItemStack getProduct() {
        return type.getProducts().getProduct(getProductSize(entity.getRNG(), happiness)).copy();
    }

    public int getHearts() {
        return (int)((((double)happiness)/ MAX_RELATIONSHIP) * 9); //0 > 9
    }

    public int getMaxRelationship() {
        return MAX_RELATIONSHIP;
    }

    public int getMinMedium() {
        return MIN_MEDIUM;
    }

    public int getMinLarge() {
        return MIN_LARGE;
    }

    /** @return a value from 0 to 2 **/
    public static int getProductSize(Random random, int happiness) {
        int hearts = 1 + (int)((((double)happiness)/ MAX_RELATIONSHIP) * 9);
        int largeChance = (100/hearts) - 9;
        int mediumChance = (50 / hearts) - 4;
        if (hearts >= 5 && random.nextInt(largeChance) == 0) return 2;
        else if (hearts >= 2 && random.nextInt(mediumChance) == 0) return 1;
        else return 0;
    }

    public static AnimalStats<?> getStats(Entity entity) {
        return CapabilityHelper.getCapabilityFromEntity(entity, ANIMAL_STATS_CAPABILITY);
    }

    public boolean hasTrait(String trait) {
        return data.containsKey(trait) || type.hasTrait(trait);
    }

    @SuppressWarnings("unchecked")
    public <T extends AnimalTraitData> T getTrait(String trait) {
        return (T) data.get(trait);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityStatsHandler.ANIMAL_STATS_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityStatsHandler.ANIMAL_STATS_CAPABILITY ? (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Town", town);
        tag.setInteger("Age", age);
        tag.setInteger("Happiness", happiness);
        tag.setInteger("HappinessDivisor", happinessDivisor);
        tag.setInteger("Hunger", hunger);
        tag.setInteger("GenericTreats", genericTreatsGiven);
        tag.setInteger("TypeTreats", typeTreatsGiven);
        tag.setInteger("Childhood", childhood);
        tag.setBoolean("HasProduct", hasProduct);
        tag.setBoolean("Loved", loved);
        tag.setBoolean("Eaten", eaten);
        tag.setBoolean("Treated", treated);
        tag.setBoolean("Special", special);
        tag.setBoolean("InSun", wasOutsideInSun);
        NBTTagCompound traits = new NBTTagCompound();
        for (Map.Entry<String, AnimalTraitData> entry: this.data.entrySet()) {
            traits.setTag(entry.getKey(), entry.getValue().serializeNBT());
        }

        tag.setTag("Traits", traits);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        town = nbt.getInteger("Town");
        age = nbt.getInteger("Age");
        happiness = nbt.getInteger("Happiness");
        happinessDivisor = nbt.getInteger("HappinessDivisor");
        if (happinessDivisor == 0) happinessDivisor = 5; //Fix the divisor
        hunger = nbt.getInteger("Hunger");
        genericTreatsGiven = nbt.getInteger("GenericTreats");
        typeTreatsGiven = nbt.getInteger("TypeTreats");
        childhood = nbt.getInteger("Childhood");
        hasProduct = nbt.getBoolean("HasProduct");
        loved = nbt.getBoolean("Loved");
        eaten = nbt.getBoolean("Eaten");
        treated = nbt.getBoolean("Treated");
        special = nbt.getBoolean("Special");
        wasOutsideInSun = nbt.getBoolean("InSun");
        NBTTagCompound traits = nbt.getCompoundTag("Traits");
        for (Map.Entry<String, AnimalTraitData> entry: this.data.entrySet()) {
            entry.getValue().deserializeNBT(traits.getCompoundTag(entry.getKey()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalStats<?> that = (AnimalStats<?>) o;
        return Objects.equals(entity.getPersistentID(), that.entity.getPersistentID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity.getPersistentID());
    }
}
