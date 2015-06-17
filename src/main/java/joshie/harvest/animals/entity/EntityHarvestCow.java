package joshie.harvest.animals.entity;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.relations.RelationshipHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHarvestCow extends EntityCow implements IAnimalTracked {
    private IRelatableDataHandler handler;
    private IAnimalData data;
    private IAnimalType type;

    public EntityHarvestCow(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        type = HFApi.ANIMALS.getType(this);
        data = HFApi.ANIMALS.newData(this);
        tasks.addTask(3, new EntityAIEat(this));
    }

    @Override
    public IRelatableDataHandler getDataHandler() {
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

    private ItemStack getMilk(EntityPlayer player) {
        Size size = Size.SMALL;
        int relationship = HFApi.RELATIONS.getAdjustedRelationshipValue(player, this);
        int chance = Math.max(1, RelationshipHelper.ADJUSTED_MAX - relationship);
        int chance2 = Math.max(1, chance / 3);
        if (rand.nextInt(chance) == 0) size = Size.LARGE;
        else if (rand.nextInt(chance2) == 0) size = Size.MEDIUM;
        return SizeableHelper.getSizeable(relationship, SizeableMeta.MILK, size);
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
            } else if (ToolHelper.isMilker(held)) {
                if (HFTrackers.getAnimalTracker().canProduceProduct(data)) {
                    ItemStack product = getMilk(player);
                    if (!player.inventory.addItemStackToInventory(product)) {
                        player.dropPlayerItemWithRandomChoice(product, false);
                    }

                    HFTrackers.getAnimalTracker().setProducedProduct(data);
                }
            }

            return false;
        }

        HFApi.RELATIONS.talkTo(player, this);
        return true;
    }

    @Override
    public EntityCow createChild(EntityAgeable ageable) {
        return new EntityHarvestCow(this.worldObj);
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
