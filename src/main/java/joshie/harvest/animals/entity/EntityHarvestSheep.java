package joshie.harvest.animals.entity;

import joshie.harvest.animals.stats.AnimalStatsHF;
import joshie.harvest.animals.stats.AnimalStatsLivestock;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SizeableHelper;
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
    private final AnimalStatsHF stats = new AnimalStatsLivestock().setType(HFApi.animals.getTypeFromString("sheep"));

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
        tasks.addTask(5, new EntityAIEat(this));
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
                ItemStack product = SizeableHelper.getWool(player, this, stats);
                ret.add(product);
                if (!worldObj.isRemote && !HFAnimals.OP_ANIMALS) {
                    setSheared(true);
                    stats.setProduced(stats.getProductsPerDay());
                    HFApi.player.getRelationsForPlayer(player).affectRelationship(EntityHelper.getEntityUUID(this), 10);
                }

                playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
                return ret;
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
        stats.deserializeNBT(compound);
    }
}
