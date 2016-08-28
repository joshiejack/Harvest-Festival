package joshie.harvest.animals.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityHarvestChicken extends EntityChicken implements IAnimalTracked {
    private final IAnimalData data;

    public EntityHarvestChicken(World world) {
        super(world);
        data = HFApi.animals.newData(this, "chicken");
        timeUntilNextEgg = Integer.MAX_VALUE;
        tasks.addTask(3, new EntityAIEat(this));
        tasks.addTask(3, new EntityAILayEgg(this));
    }

    @Override
    public IAnimalData getData() {
        return data;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (stack != null) {
            if (HFApi.animals.canEat(stack, data.getType().getFoodTypes())) {
                if (!worldObj.isRemote) {
                    data.feed(player);
                }

                return true;
            }

            return false;
        }

        startRiding(player);
        return true;
    }

    @Override
    public EntityChicken createChild(EntityAgeable ageable) {
        return new EntityHarvestChicken(worldObj);
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