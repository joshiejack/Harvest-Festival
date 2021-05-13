package uk.joshiejack.harvestcore.tile;

import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderItem;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.DoubleMachine;
import uk.joshiejack.penguinlib.tile.machine.TileMachineSimple;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@PenguinLoader("furnace")
public class TileFurnace extends TileMachineSimple implements DoubleMachine {
    public static final HolderRegistry<Recipe> registry = new HolderRegistry<>(Recipe.NULL);
    private static final HolderItem COAL = new HolderItem(Items.COAL);

    public TileFurnace() {
        super("half_hour");
    }

    @Override
    public long getOperationalTime() {
        return registry.getValue(handler.getStackInSlot(0)).duration;
    }

    @Override
    protected void setupMachine(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        ItemStack stack = StackHelper.toStack(held, registry.getValue(held).amount);
        handler.setStackInSlot(0, stack);
        InventoryHelper.takeItemsInInventory(player, COAL, 1);
        InventoryHelper.takeItemsInInventory(player, Holder.getFromStack(held), stack.getCount());
    }

    @Override
    public void finishMachine() {
        handler.setStackInSlot(0, registry.getValue(handler.getStackInSlot(0)).result.copy());
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return registry.getValue(stack) != Recipe.NULL;
    }

    @Override
    public boolean isItemValidForInsertion(int slot, EntityPlayer player, EnumHand hand) {
        if (!InventoryHelper.hasInInventory(player, COAL, 1)) return false;
        else {
            ItemStack input = player.getHeldItem(hand);
            Recipe result = registry.getValue(input); //You need to have the minimum amount + Coal
            return result != Recipe.NULL && InventoryHelper.hasInInventory(player, input, result.amount);
        }
    }

    public static class Recipe {
        public static Recipe NULL = new Recipe(ItemStack.EMPTY, 24000L, 0);
        private final ItemStack result;
        private final long duration;
        private final int amount;

        public Recipe(ItemStack result, long duration, int amount) {
            this.result = result;
            this.duration = duration;
            this.amount = amount;
        }

        public ItemStack getResult() {
            return result;
        }
    }
}

