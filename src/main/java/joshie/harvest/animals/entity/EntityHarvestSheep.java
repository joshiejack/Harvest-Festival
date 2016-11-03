package joshie.harvest.animals.entity;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalHandler.AnimalAI;
import joshie.harvest.api.animals.IAnimalHandler.AnimalType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityHarvestSheep extends EntitySheep {
    private final AnimalStats<NBTTagCompound> stats = HFApi.animals.newStats(AnimalType.LIVESTOCK);

    public EntityHarvestSheep(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initEntityAI() {
        entityAIEatGrass = new EntityAIEatGrass(this);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.WHEAT, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        HFApi.animals.getEntityAI(this, AnimalAI.EAT_GRASS, true);
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
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

        return true;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        if (!isChild()) {
            EntityPlayer player = worldObj.getClosestPlayerToEntity(this, 178D);
            if (player != null) {
                ret.add(stats.getType().getProduct(player, stats));
                if (!worldObj.isRemote) {
                    setSheared(true);
                    stats.setProduced(stats.getProductsPerDay());
                    HFApi.player.getRelationsForPlayer(player).affectRelationship(EntityHelper.getEntityUUID(this), stats.getType().getRelationshipBonus(AnimalAction.CLAIM_PRODUCT));
                }

                playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            }
        }

        return ret;
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        return new EntityHarvestSheep(worldObj);
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
