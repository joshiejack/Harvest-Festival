package joshie.harvest.core.helpers;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

import static joshie.harvest.core.helpers.InventoryHelper.SearchType.*;

public class InventoryHelper {
    public static Item getHeldItem(EntityPlayer player) {
        if (player.getHeldItemMainhand() == null) return null;
        return player.getHeldItemMainhand().getItem();
    }

    public enum SearchType {
        FLOWER, HOE, BUCKET
    }

    public static final Matcher<String> ORE_DICTIONARY = new Matcher<String>() {
        @Override
        public boolean matches(ItemStack stack, String string) {
            if (stack == null) return false;
            return InventoryHelper.isOreName(stack, string);
        }
    };

    public static final Matcher<ItemStack> ITEM_STACK = new Matcher<ItemStack>() {
        @Override
        public boolean matches(ItemStack stack, ItemStack stack2) {
            if (stack == null) return false;
            return stack.isItemEqual(stack2);
        }
    };

    public static final Matcher<SearchType> SPECIAL = new Matcher<SearchType>() {
        @Override
        public boolean matches(ItemStack stack, SearchType type) {
            if (stack == null) return false;
            if (type.equals(FLOWER)) {
                return stack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER) || stack.getItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER) || HFCore.FLOWERS.getStackFromEnum(FlowerType.GODDESS).isItemEqual(stack);
            } else if (type.equals(HOE)) {
                return stack.getItem() instanceof ItemHoe;
            } else if (type.equals(BUCKET)) {
                return stack.getItem() instanceof ItemBucket;
            }

            return false;
        }
    };

    public static <T> void takeItems(EntityPlayer player, T taking, int amount, Matcher<T> matcher) {
        int toTake = amount;
        for (int i = 0; i < player.inventory.mainInventory.length && toTake > 0; i++) {
            ItemStack stack = player.inventory.mainInventory[i];
            if (stack != null && matcher.matches(stack, taking)) {
                ItemStack taken = stack.splitStack(toTake);
                toTake -= taken.stackSize;
                if (stack.stackSize <= 0) player.inventory.mainInventory[i] = null; //Clear
                if (toTake <= 0) return; //No further processing neccessary
            }
        }
    }

    public static void takeItems(EntityPlayer player, ItemStack taking) {
        takeItems(player, taking, taking.stackSize, ITEM_STACK);
    }

    public static void takeItems(EntityPlayer player, String ore, int amount) {
        takeItems(player, ore, amount, ORE_DICTIONARY);
    }

    public static void takeItems(EntityPlayer player, SearchType search, int amount) {
        takeItems(player, search, amount, SPECIAL);
    }

    @SuppressWarnings("unchecked")
    public static <S> boolean hasInInventory(EntityPlayer player, Matcher matcher, S search, int... amount) {
        int count = amount == null || amount.length == 0 ? 1 : amount[0];
        return getCount(player, search, matcher) >= count;
    }

    @SuppressWarnings("unchecked")
    public static <S> boolean takeItemsInInventory(EntityPlayer player, Matcher matcher, S search, int... amount) {
        int count = amount == null || amount.length == 0 ? 1 : amount[0];
        if (hasInInventory(player, matcher, search, count)) {
            takeItems(player, search, count, matcher);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <S> boolean isHolding(EntityPlayer player, Matcher matcher, S search, int... amount) {
        int count = amount == null || amount.length == 0 ? 1 : amount[0];
        ItemStack held = player.getHeldItemMainhand();
        if (held != null && matcher.matches(held, search)) {
            return held.stackSize >= count;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <S> boolean takeItemsIfHeld(EntityPlayer player, Matcher matcher, S search, int... amount) {
        int count = amount == null || amount.length == 0 ? 1 : amount[0];
        if (isHolding(player, matcher, search, count)) {
            takeItems(player, search, count, matcher);
            return true;
        }

        return false;
    }

    private static <T> int getCount(EntityPlayer player, T taking, Matcher<T> matcher) {
        int count = 0;
        for (ItemStack item: player.inventory.mainInventory) {
            if (item == null) continue;
            if (matcher.matches(item, taking)) {
                count += item.stackSize;
            }
        }

        return count;
    }

    public static int getCount(EntityPlayer player, ItemStack stack) {
        return getCount(player, stack, ITEM_STACK);
    }
    
    public static int getCount(EntityPlayer player, String ore) {
        return getCount(player, ore, ORE_DICTIONARY);
    }
    
    public static boolean isOreName(ItemStack stack, String ore) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i: ids) {
            String name = OreDictionary.getOreName(i);
            if (name.equals(ore)) {
                return true;
            }
        }
        
        return false;
    }

    public abstract static class Matcher<T> {
        public abstract boolean matches(@Nonnull ItemStack stack, T t);
    }
}
