package joshie.harvest.animals.entity;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.relations.IDataHandler;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.relations.RelationshipHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHarvestSheep extends EntitySheep implements IAnimalTracked {
    private IAnimalData data;
    private IAnimalType type;

    public EntityHarvestSheep(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        data = HFApi.ANIMALS.newData(this);
        type = HFApi.ANIMALS.getType(this);
        tasks.addTask(3, new EntityAIEat(this));
        tasks.removeTask(field_146087_bs);
    }
    
    @Override
    public IDataHandler getDataHandler() {
        return RelationshipHelper.getHandler("entity");
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
    public boolean interact(EntityPlayer player) {
        ItemStack held = player.getCurrentEquippedItem();
        if (held != null) {
            if (HFApi.ANIMALS.canEat(type.getFoodTypes(), held)) {
                if (!worldObj.isRemote) {
                    data.feed(player);
                }

                return true;
            }

            return false;
        }

        HFApi.RELATIONS.talkTo(player, this);
        return true;
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
