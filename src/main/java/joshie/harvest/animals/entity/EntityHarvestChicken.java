package joshie.harvest.animals.entity;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.HFTrackers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Set;

public class EntityHarvestChicken extends EntityChicken implements IAnimalTracked<EntityHarvestChicken> {
    private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS);
    private final IAnimalData data;
    private EntityPlayer toLovePlayer;
    private int toLoveTicker;

    public EntityHarvestChicken(World world) {
        super(world);
        timeUntilNextEgg = Integer.MAX_VALUE;
        data = HFApi.animals.newData(this, "chicken");
    }

    @Override
    protected void initEntityAI()  {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        tasks.addTask(3, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        tasks.addTask(5, new EntityAIEat(this));
        tasks.addTask(5, new EntityAILayEgg(this));
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0, true, false));
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public IAnimalData getData() {
        return data;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (stack != null) {
            if (HFApi.animals.canEat(stack, data.getType().getFoodTypes())) {
                if (data.isHungry()) {
                    stack.splitStack(1);
                    if (!worldObj.isRemote) data.feed(player);
                }

                return true;
            }

            return false;
        }

        return true;
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
                HFTrackers.getPlayerTrackerFromPlayer(toLovePlayer).getRelationships().affectRelationship(toLovePlayer, getUUID(), 100);
                toLovePlayer = null;
            }
        }
    }

    @Override
    public void setInLove(EntityPlayer player) {
        toLovePlayer = player;
        toLoveTicker = 20;
    }

    /*################### Data ############## */
    @Override
    public void writeSpawnData(ByteBuf buffer) {
        data.toBytes(buffer);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        data.fromBytes(buffer);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        data.readFromNBT(nbt);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        data.writeToNBT(nbt);
    }
}