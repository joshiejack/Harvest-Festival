package joshie.harvest.buildings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import java.util.Random;

import static joshie.harvest.buildings.LootHelper.FakeInventory.INVENTORY;

public class LootHelper {
    protected static class FakeInventory implements IInventory {
        public static final FakeInventory INVENTORY = new FakeInventory();

        private ItemStack result = null;

        @Override
        public int getSizeInventory() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return result;
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            return null;
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return null;
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            result = stack;
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public void markDirty() {}

        @Override
        public boolean isUsableByPlayer(EntityPlayer player) {
            return false;
        }

        @Override
        public void openInventory(EntityPlayer player) { }

        @Override
        public void closeInventory(EntityPlayer player) {}

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return true;
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {}

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {}

        @Override
        public String getName() {
            return "Fake";
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentString(getName());
        }
    }

    public static ItemStack getStack(World world, EntityPlayer player, ResourceLocation lootTable) {
        long lootTableSeed = world.rand.nextLong();
        LootTable loottable = world.getLootTableManager().getLootTableFromLocation(lootTable);
        Random random;

        if (lootTableSeed == 0L) {
            random = new Random();
        } else {
            random = new Random(lootTableSeed);
        }

        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)world);

        if (player != null) {
            lootcontext$builder.withLuck(player.getLuck());
        }

        loottable.fillInventory(INVENTORY, random, lootcontext$builder.build());
        ItemStack result = INVENTORY.getStackInSlot(0);
        if (result != null) result = result.copy();
        INVENTORY.setInventorySlotContents(0, null);
        return  result;
    }
}