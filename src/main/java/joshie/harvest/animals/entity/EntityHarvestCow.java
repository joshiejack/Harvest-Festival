package joshie.harvest.animals.entity;

import joshie.harvest.animals.stats.AnimalStatsHF;
import joshie.harvest.animals.stats.AnimalStatsMilkable;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import static joshie.harvest.api.animals.IAnimalHandler.ANIMAL_STATS_CAPABILITY;

public class EntityHarvestCow extends EntityCow {
    private final AnimalStatsHF stats = new AnimalStatsMilkable().setType(HFApi.animals.getTypeFromString("cow"));

    public EntityHarvestCow(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.WHEAT, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        tasks.addTask(5, new EntityAIEat(this));
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0, true, false));
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (worldObj.rand.nextFloat() < 0.33F) {
            SoundEvent s = getAmbientSound();
            if (s != null) {
                playSound(s, 2F, getSoundPitch());
            }

            HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().talkTo(player, EntityHelper.getEntityUUID(this));
            return true;
        }

        return false;
    }

    @Override
    public EntityCow createChild(EntityAgeable ageable) {
        return new EntityHarvestCow(this.worldObj);
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
        else if (compound.hasKey("CurrentLifespan")) stats.deserializeNBT(compound);
        //TODO: Remove in 0.7+ ^^^....^^^^^
    }
}