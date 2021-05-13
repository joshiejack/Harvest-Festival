package uk.joshiejack.penguinlib.tile.machine;

import net.minecraft.world.WorldServer;
import uk.joshiejack.penguinlib.data.database.registries.TimeUnitRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;

public abstract class TileMachineSimple extends TileMachine {
    private final String time;

    public TileMachineSimple(String time) {
        super(1);
        this.time = time;
    }

    @Override
    public long getOperationalTime() {
        return TimeUnitRegistry.get(time);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return !isActive();
    }

    public boolean isItemValidForInsertion(int slot, EntityPlayer player, EnumHand hand) {
        return isStackValidForInsertion(slot, player.getHeldItem(hand));
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        if (!handler.getStackInSlot(0).isEmpty()) {
            if (!isActive()) {
                ItemHandlerHelper.giveItemToPlayer(player, handler.getStackInSlot(0));
                handler.setStackInSlot(0, ItemStack.EMPTY);
                world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
                onMachineEmptied();
                return true;
            }
        } else if (isItemValidForInsertion(0, player, hand)) {
            setupMachine(player, hand);
            startMachine(player);
            world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            return true;
        }

        return false;
    }

    @Override
    public void onContentsChanged(int slot) {
        ItemStack inSlot = handler.getStackInSlot(slot);
        if (isStackValidForInsertion(slot, inSlot)) {
            startMachine(FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos));
            world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
        }
    }

    protected void setupMachine(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand).splitStack(1);
        handler.setStackInSlot(0, stack);
    }

    protected void onMachineEmptied() {}
}

