package joshie.harvest.fishing.tile;

import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.core.helpers.FakePlayerHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.fishing.FishingAPI;
import joshie.harvest.fishing.FishingHelper;
import joshie.harvest.fishing.HFFishing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

import javax.annotation.Nonnull;

public class TileTrap extends TileSingleStack {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.MAIN) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFFishing.AQUATIC_BLOCKS && HFFishing.AQUATIC_BLOCKS.getEnumFromState(state).isTrap();
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileTrap trap = (TileTrap) world.getTileEntity(pos);
            if (trap.stack != null && FishingAPI.INSTANCE.isBait(trap.stack)) {
                if (trap.isSurroundedByWater(world, pos)) {
                    LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
                    lootcontext$builder.withLootedEntity(FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos));
                    if (FishingHelper.isWater(world, pos.down())) lootcontext$builder.withLuck(1F);
                    for (ItemStack itemstack : world.getLootTableManager().getLootTableFromLocation(trap.getLootTable()).generateLootForPools(world.rand, lootcontext$builder.build())) {
                        trap.baited = false;
                        trap.stack = itemstack.copy();
                    }

                    trap.saveAndRefresh();
                }
            }
        }
    };
    private boolean baited = false;

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, @Nonnull ItemStack place) {
        if (FishingAPI.INSTANCE.isBait(place)) {
            stack = place.splitStack(1);
            baited = true;
            saveAndRefresh();
            return true;
        } else if (!FishingAPI.INSTANCE.isBait(stack)) {
            FishingHelper.track(stack, player);
            SpawnItemHelper.addToPlayerInventory(player, stack);
            baited = false;
            stack = ItemStack.EMPTY;
            saveAndRefresh();
            return true;
        }

        return false;
    }

    public boolean isBaited() {
        return baited;
    }

    private ResourceLocation getLootTable() {
        return world.rand.nextInt(4) == 0 ? FishingHelper.getFishingTable(world, pos) : LootStrings.TRAP_JUNK;
    }

    private boolean isSurroundedByWater(World world, BlockPos pos) {
        return FishingHelper.isWater(world, pos.east(), pos.west(), pos.south(), pos.north(), pos.up());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        baited = nbt.getBoolean("Baited");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Baited", baited);
        return super.writeToNBT(nbt);
    }
}
