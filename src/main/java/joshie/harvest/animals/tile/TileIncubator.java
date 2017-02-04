package joshie.harvest.animals.tile;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.EntityHarvestChicken;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.tile.TileFillableSizedFaceable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TileIncubator extends TileFillableSizedFaceable {
    private static final int MAX_FILL = 7;
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.POST) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFAnimals.SIZED;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileIncubator incubator = (TileIncubator) world.getTileEntity(pos);
            if (incubator.fillAmount > 0 && incubator.owner != null) {
                incubator.fillAmount--;
                if (incubator.fillAmount == 0) {
                    for (int i = 0; i < incubator.getBabyAmount(); i++) {
                        EntityHarvestChicken baby = new EntityHarvestChicken(world);
                        baby.setPositionAndUpdate(pos.getX(), pos.up().getY() + world.rand.nextDouble(), pos.getZ());
                        baby.setGrowingAge(-(24000 * HFAnimals.AGING_TIMER));
                        AnimalStats stats = EntityHelper.getStats(baby);
                        if (stats != null) {
                            stats.setOwner(incubator.owner);
                            HFTrackers.getPlayerTracker(world, incubator.owner).getRelationships().copyRelationship(EntityHelper.getPlayerFromUUID(incubator.owner), incubator.relationship, EntityHelper.getEntityUUID(baby), 50D);
                        }

                        world.spawnEntityInWorld(baby);
                    }

                    incubator.owner = null; //Clear out owner
                }

                incubator.saveAndRefresh();
            }
        }
    };


    private UUID owner;
    private int relationship;

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack held) {
        if (ToolHelper.isEgg(held)) {
            if (fillAmount == 0) {
                setFilled(HFApi.sizeable.getSize(held), MAX_FILL);
                NBTTagCompound tag = held.getSubCompound("Data", true);
                if (tag.hasKey("Relationship")) {
                    relationship = tag.getInteger("Relationship");
                } else relationship = 0;

                if (tag.hasKey("Owner")) {
                    owner = UUID.fromString(tag.getString("Owner"));
                } else owner = EntityHelper.getPlayerUUID(player);

                held.splitStack(1);
                return true;
            }
        }

        return false;
    }

    private int getBabyAmount() {
        int amount = 1;
        if (size == Size.MEDIUM && worldObj.rand.nextInt(20) == 0) amount++;
        if (size == Size.LARGE && worldObj.rand.nextInt(10) == 0) amount++;
        if (size == Size.LARGE && worldObj.rand.nextInt(50) == 0) amount++;
        return amount;
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
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Relationship", relationship);
        if (owner != null) {
            nbt.setString("Owner", owner.toString());
        }

        return super.writeToNBT(nbt);
    }
}