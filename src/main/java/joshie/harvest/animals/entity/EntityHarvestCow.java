package joshie.harvest.animals.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IMilkable;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityHarvestCow extends EntityCow implements IAnimalTracked, IMilkable {
    private final IAnimalData data;

    public EntityHarvestCow(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        data = HFApi.animals.newData(this, "cow");
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
    public IAnimalData getData() {
        return data;
    }

    @Override
    public boolean canMilk() {
        return data.canProduce();
    }

    @Override
    public void milk(EntityPlayer player) {
        ItemStack product = SizeableHelper.getMilk(player, this);
        if (!player.inventory.addItemStackToInventory(product)) {
            player.dropItem(product, false);
        }

        if (!worldObj.isRemote && !HFAnimals.OP_ANIMALS) {
            data.setProduced(getData().getProductsPerDay());
        }
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

        if (worldObj.rand.nextFloat() < 0.33F) {
            SoundEvent s = getAmbientSound();
            if (s != null) {
                playSound(s, 2F, getSoundPitch());
            }

            HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().talkTo(player, this);
            return true;
        }

        return false;
    }

    @Override
    public EntityCow createChild(EntityAgeable ageable) {
        return new EntityHarvestCow(this.worldObj);
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