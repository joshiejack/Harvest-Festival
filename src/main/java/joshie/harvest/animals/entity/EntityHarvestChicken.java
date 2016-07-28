package joshie.harvest.animals.entity;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityHarvestChicken extends EntityChicken implements IAnimalTracked {
    private IAnimalData data;
    private IAnimalType type;

    public EntityHarvestChicken(World world) {
        super(world);
        type = HFApi.animals.getType(this);
        data = HFApi.animals.newData(this);
        timeUntilNextEgg = Integer.MAX_VALUE;
        tasks.addTask(3, new EntityAIEat(this));
        tasks.addTask(3, new EntityLayEgg(this));
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

        startRiding(player);
        return true;
    }

    @Override
    public EntityChicken createChild(EntityAgeable ageable) {
        return new EntityHarvestChicken(worldObj);
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