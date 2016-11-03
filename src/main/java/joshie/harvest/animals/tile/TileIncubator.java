package joshie.harvest.animals.tile;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.tile.TileFillableSizedFaceable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;

public class TileIncubator extends TileFillableSizedFaceable {
    private static final int MAX_FILL = 7;
    private UUID owner;
    private int relationship;

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack held) {
        if (ToolHelper.isEgg(held)) {
            if (fillAmount == 0) {
                setFilled(HFApi.sizeable.getSize(held), MAX_FILL);
                NBTTagCompound tag = held.getSubCompound("Data", true);
                //TODO: Remove in 0.7+
                if (tag.hasKey("Relationship")) {
                    relationship = tag.getInteger("Relationship");
                    if (tag.hasKey("Owner")) {
                        owner = UUID.fromString(tag.getString("Owner"));
                    } else owner = EntityHelper.getPlayerUUID(player);
                } else {
                    owner = EntityHelper.getPlayerUUID(player);
                    if (tag.hasKey("Mother")) {
                        UUID mother = UUID.fromString(tag.getString("Mother"));
                        relationship = HFApi.player.getRelationsForPlayer(player).getRelationship(mother);
                    }
                }

                held.splitStack(1);
                return true;
            }
        }

        return false;
    }

    @Override
    public void newDay() {
        if (fillAmount > 0) {
            fillAmount--;

            if (fillAmount == 0) {
                int amount = 1;
                if (size == Size.MEDIUM && worldObj.rand.nextInt(20) == 0) amount++;
                if (size == Size.LARGE && worldObj.rand.nextInt(10) == 0) amount++;
                if (size == Size.LARGE && worldObj.rand.nextInt(50) == 0) amount++;
                for (int i = 0; i < amount; i++) {
                    EntityHarvestChicken baby = new EntityHarvestChicken(worldObj);
                    baby.setPositionAndUpdate(getPos().getX() + 3 * getWorld().rand.nextDouble(), getPos().getY() + getWorld().rand.nextDouble(), getPos().getZ() + 3 * getWorld().rand.nextDouble());
                    baby.setGrowingAge(-(24000 * HFAnimals.AGING_TIMER));
                    if (owner != null) {
                        AnimalStats stats = EntityHelper.getStats(baby);
                        if (stats != null) {
                            stats.setOwner(owner);
                            HFTrackers.getPlayerTracker(worldObj, owner).getRelationships().copyRelationship(EntityHelper.getPlayerFromUUID(owner), relationship, EntityHelper.getEntityUUID(baby), 50D);
                        }
                    }

                    worldObj.spawnEntityInWorld(baby);
                }
            }

            markTileForUpdate(this);
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        relationship = nbt.getInteger("Relationship");
        if (nbt.hasKey("Owner")) {
            owner = UUID.fromString(nbt.getString("Owner"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Relationship", relationship);
        if (owner != null) {
            nbt.setString("Owner", owner.toString());
        }

        return super.writeToNBT(nbt);
    }
}