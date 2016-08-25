package joshie.harvest.animals.entity;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SizeableHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityHarvestSheep extends EntitySheep implements IAnimalTracked {
    private IAnimalData data;
    private IAnimalType type;

    public EntityHarvestSheep(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        type = HFApi.animals.getType(this);
        data = HFApi.animals.newData(this);
        tasks.addTask(3, new EntityAIEat(this));
        tasks.removeTask(entityAIEatGrass);
    }
    
    @Override
    public IRelatableDataHandler getDataHandler() {
        return HFApi.player.getRelationshipHelper().getDataHandler("entity");
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
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (stack != null) {
            if (HFApi.animals.canEat(stack, type.getFoodTypes())) {
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

            HFTrackers.getPlayerTracker(player).getRelationships().talkTo(player, this);
            return true;
        }

        return true;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        EntityPlayer player = worldObj.getClosestPlayerToEntity(this, 178D);
        if (player != null) {
            ItemStack product = SizeableHelper.getWool(player, this);
            ret.add(product);
            if (!worldObj.isRemote && !HFAnimals.OP_ANIMALS) {
                setSheared(true);
                data.setProduced();
            }

            playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            return ret;
        }

        return ret;
    }

    @Override
    public EntitySheep createChild(EntityAgeable ageable) {
        return new EntityHarvestSheep(worldObj);
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
