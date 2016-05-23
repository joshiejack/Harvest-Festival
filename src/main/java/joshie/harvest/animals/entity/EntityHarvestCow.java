package joshie.harvest.animals.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.animals.IMilkable;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityHarvestCow extends EntityCow implements IAnimalTracked, IEntityAdditionalSpawnData, IMilkable {
    private IAnimalData data;
    private IAnimalType type;

    public EntityHarvestCow(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        type = HFApi.animals.getType(this);
        data = HFApi.animals.newData(this);
        tasks.addTask(3, new EntityAIEat(this));
    }

    @Override
    public IRelatableDataHandler getDataHandler() {
        return HFApi.relations.getDataHandler("entity");
    }

    @Override
    public IRelatable getRelatable() {
        return this;
    }

    @Override
    public IAnimalData getData() {
        return data;
    }

    @Override
    public IAnimalType getType() {
        return type;
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

        if (!worldObj.isRemote) {
            data.setProduced();
        }
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack held = player.getActiveItemStack();
        if (held != null) {
            if (HFApi.animals.canEat(held, type.getFoodTypes())) {
                if (!worldObj.isRemote) {
                    data.feed(player);
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

            HFApi.relations.talkTo(player, this);
            return true;
        }

        return false;
    }

    @Override
    public EntityCow createChild(EntityAgeable ageable) {
        return new EntityHarvestCow(this.worldObj);
    }

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