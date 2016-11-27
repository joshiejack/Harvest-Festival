package joshie.harvest.animals.entity;

import com.google.common.collect.Sets;
import joshie.harvest.animals.entity.ai.EntityAIFindShelterOrSun;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalHandler.AnimalAI;
import joshie.harvest.api.animals.IAnimalHandler.AnimalType;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Set;

import static joshie.harvest.api.animals.IAnimalHandler.ANIMAL_STATS_CAPABILITY;

public class EntityHarvestChicken extends EntityChicken {
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private final AnimalStats<NBTTagCompound> stats = HFApi.animals.newStats(AnimalType.POULTRY);
    private EntityPlayer toLovePlayer;
    private int toLoveTicker;

    public EntityHarvestChicken(World world) {
        super(world);
        timeUntilNextEgg = Integer.MAX_VALUE;
    }

    @Override
    protected void initEntityAI()  {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        tasks.addTask(3, new EntityAIFindShelterOrSun(this));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        HFApi.animals.getEntityAI(this, AnimalAI.EAT, true);
        HFApi.animals.getEntityAI(this, AnimalAI.EGGS, true);
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    }

    @Override
    public EntityHarvestChicken createChild(EntityAgeable ageable) {
        return new EntityHarvestChicken(worldObj);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (toLovePlayer != null) {
            if (toLoveTicker >= 0) toLoveTicker--;
            else {
                HFTrackers.getPlayerTrackerFromPlayer(toLovePlayer).getRelationships().talkTo(RelationshipType.ANIMAL, toLovePlayer, EntityHelper.getEntityUUID(this));
                toLovePlayer = null; //Player no more!!!!!!!!
            }
        }
    }

    @Override
    public void setInLove(EntityPlayer player) {
        toLovePlayer = player;
        toLoveTicker = 20;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ANIMAL_STATS_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
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
}