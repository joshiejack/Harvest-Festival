package uk.joshiejack.piscary.tile;

import uk.joshiejack.penguinlib.tile.inventory.TileSingleStack;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;
import uk.joshiejack.piscary.block.BlockTrap;
import uk.joshiejack.piscary.item.PiscaryItems;
import uk.joshiejack.piscary.loot.PiscaryLootTables;
import uk.joshiejack.piscary.item.ItemBait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

@PenguinLoader("fish_trap")
public class TileTrap extends TileSingleStack {
    private long timeCaught = 0;

    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return stack.getItem() == PiscaryItems.BAIT && !isBaited();
    }

    public boolean isSlotValidForExtraction(int slot, int amount) {
        return !isBaited();
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!isBaited()) {
            if (held.getItem() == PiscaryItems.BAIT && handler.getStackInSlot(0).isEmpty()) {
                if (!world.isRemote) {
                    handler.setStackInSlot(0, held.copy());
                    handler.getStackInSlot(0).setCount(1);
                    held.shrink(1);
                }

                world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                return true;
            } else if (!handler.getStackInSlot(0).isEmpty()) {
                if (!world.isRemote) {
                    ItemHandlerHelper.giveItemToPlayer(player, handler.getStackInSlot(0).copy());
                    handler.setStackInSlot(0, ItemStack.EMPTY);
                }

                if (attemptToBreakTrap()) world.setBlockToAir(getPos());
                else world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                return true;
            }
        }

        return false;
    }

    private boolean attemptToBreakTrap() {
        long difference = world.getWorldTime() - timeCaught; //Always break after two hours, 50% between 30 minutes and 2 hours, 20% otherwise,
        return difference >= 2000 || (difference < 500 && world.rand.nextInt(5) <= 1) || (difference >= 500 && world.rand.nextInt(2) == 0);
    }

    public boolean isBaited() {
        return handler.getStackInSlot(0).getItem() == PiscaryItems.BAIT;
    }

    public BlockTrap.Trap getTrapEnum() {
        ItemStack bait = handler.getStackInSlot(0);
        return bait.getItemDamage() == ItemBait.Bait.BASIC.ordinal() ? BlockTrap.Trap.BAITED : BlockTrap.Trap.BAITED_VANILLA;
    }

    @Override
    public void updateTick() {
        if (isBaited() && isSurroundedByWater()) {
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
            lootcontext$builder.withLootedEntity(FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos));
            if (TerrainHelper.isWater(world, pos.down())) lootcontext$builder.withLuck(1F);
            for (ItemStack itemstack : world.getLootTableManager().getLootTableFromLocation(getLootTable()).generateLootForPools(world.rand, lootcontext$builder.build())) {
                handler.setStackInSlot(0, itemstack.copy());
                timeCaught = world.getWorldTime();
            }

            world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
        }
    }

    private ResourceLocation getLootTable() {
        //If we have the vanilla bait, catch from the vanilla fish list
        return world.rand.nextInt(4) == 0 ? PiscaryItems.BAIT.getEnumFromStack(handler.getStackInSlot(0)) == ItemBait.Bait.VANILLA  ? LootTableList.GAMEPLAY_FISHING_FISH :
                PiscaryLootTables.FISH : PiscaryLootTables.JUNK;
    }

    private boolean isSurroundedByWater() {
        return TerrainHelper.isWater(world, pos.east(), pos.west(), pos.south(), pos.north(), pos.up());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        timeCaught = nbt.getLong("TimeCaught");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("TimeCaught", timeCaught);
        return super.writeToNBT(nbt);
    }
}
