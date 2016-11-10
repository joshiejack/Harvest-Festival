package joshie.harvest.fishing.tile;

import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.core.helpers.FakePlayerHelper;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.fishing.FishingAPI;
import joshie.harvest.fishing.FishingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

public class TileTrap extends TileSingleStack {
    private boolean baited = false;

    @Override
    public boolean onRightClicked(EntityPlayer player, ItemStack place) {
        if (stack == null && place != null && FishingAPI.INSTANCE.isBait(place)) {
            stack = place.splitStack(1);
            baited = true;
            saveAndRefresh();
            return true;
        } else if (stack != null && !FishingAPI.INSTANCE.isBait(stack)) {
            SpawnItemHelper.addToPlayerInventory(player, stack);
            baited = false;
            stack = null;
            saveAndRefresh();
            return true;
        }

        return false;
    }

    public boolean isBaited() {
        return baited;
    }

    private ResourceLocation getLootTable() {
        return worldObj.rand.nextInt(4) == 0 ? FishingHelper.getFishingTable(worldObj, pos) : LootStrings.TRAP_JUNK;
    }

    @Override
    public void newDay() {
        if (isSurroundedByWater(worldObj, pos)) {
            if (stack != null && FishingAPI.INSTANCE.isBait(stack)) {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) worldObj);
                lootcontext$builder.withLootedEntity(FakePlayerHelper.getFakePlayerWithPosition((WorldServer) worldObj, getPos()));
                if (FishingHelper.isWater(worldObj, pos.down())) lootcontext$builder.withLuck(1F);
                for (ItemStack itemstack : worldObj.getLootTableManager().getLootTableFromLocation(getLootTable()).generateLootForPools(worldObj.rand, lootcontext$builder.build())) {
                    baited = false;
                    stack = itemstack.copy();
                }

                saveAndRefresh();
                MCServerHelper.markTileForUpdate(this);
            }
        }
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Baited", baited);
        return super.writeToNBT(nbt);
    }
}
