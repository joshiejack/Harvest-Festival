package joshie.harvest.animals.entity;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import joshie.harvest.animals.entity.ai.EntityAIEat;
import joshie.harvest.animals.entity.ai.EntityAIFindShelterOrSun;
import joshie.harvest.animals.entity.ai.EntityAILayEgg;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalHandler.AnimalType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static joshie.harvest.api.animals.IAnimalHandler.ANIMAL_STATS_CAPABILITY;

public class EntityHarvestChicken extends EntityChicken implements IEntityAdditionalSpawnData {
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private final AnimalStats<NBTTagCompound> stats = HFApi.animals.newStats(AnimalType.POULTRY);
    private boolean tickToLove;
    private int toLoveTicker;

    public EntityHarvestChicken(World world) {
        super(world);
        timeUntilNextEgg = Integer.MAX_VALUE;
    }

    @Override
    protected void initEntityAI()  {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        tasks.addTask(2, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        tasks.addTask(3, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(4, new EntityAIEat(this));
        tasks.addTask(5, new EntityAILayEgg(this));
        tasks.addTask(6, new EntityAIFindShelterOrSun(this));
        tasks.addTask(7, new EntityAIWander(this, 1.0D));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(9, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    }

    @Override
    @Nonnull
    public EntityHarvestChicken createChild(EntityAgeable ageable) {
        return new EntityHarvestChicken(world);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (tickToLove) {
            if (toLoveTicker >= 0) toLoveTicker--;
            else {
                if (!stats.performTest(AnimalTest.BEEN_LOVED)) {
                    stats.performAction(world, ItemStack.EMPTY, AnimalAction.PETTED); //Love <3
                }

                tickToLove = false;
            }
        }
    }

    @Override
    public void setInLove(@Nullable EntityPlayer player) {
        tickToLove = true;
        toLoveTicker = 20;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ANIMAL_STATS_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("ConstantConditions, unchecked")
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ANIMAL_STATS_CAPABILITY ? (T) stats : super.getCapability(capability, facing);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Stats", stats.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Stats")) stats.deserializeNBT(compound.getCompoundTag("Stats"));
        //TODO: Remove in 0.7+
        else if (compound.hasKey("CurrentLifespan")) stats.deserializeNBT(compound);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, stats.serializeNBT());
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        stats.setEntity(this);
        stats.deserializeNBT(ByteBufUtils.readTag(buffer));
    }
}