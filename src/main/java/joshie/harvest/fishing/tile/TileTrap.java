package joshie.harvest.fishing.tile;

import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.core.helpers.FakePlayerHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.fishing.FishingAPI;
import joshie.harvest.fishing.FishingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

public class TileTrap extends TileSingleStack {
    @Override
    public boolean onRightClicked(EntityPlayer player, ItemStack place) {
        if (stack == null && place != null && FishingAPI.INSTANCE.isBait(place)) {
            stack = place.splitStack(1);
            return true;
        } else if (stack != null) {
            SpawnItemHelper.addToPlayerInventory(player, stack);
            return true;
        }

        return false;
    }

    @Override
    public void newDay() {
        if (isOnSeafloor(worldObj, getPos())) {
            if (stack != null && FishingAPI.INSTANCE.isBait(stack)) {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) worldObj);
                lootcontext$builder.withLootedEntity(FakePlayerHelper.getFakePlayerWithPosition((WorldServer) worldObj, getPos()));
                for (ItemStack itemstack : worldObj.getLootTableManager().getLootTableFromLocation(FishingHelper.getFishingTable(worldObj, getPos())).generateLootForPools(worldObj.rand, lootcontext$builder.build())) {
                    stack = itemstack.copy();
                }
            }
        }
    }

    private boolean isOnSeafloor(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP) && FishingHelper.isWater(world, pos.up());
    }
}
