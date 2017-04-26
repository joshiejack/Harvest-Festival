package joshie.harvest.core.helpers;

import joshie.harvest.tools.ToolHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

import static joshie.harvest.core.helpers.InventoryHelper.SearchType.*;

public class InventoryHelper {
    public enum SearchType {
        FLOWER, HOE, BUCKET, SHEARS, WOOL, PICKAXE
    }

    public static final Matcher<String> ORE_DICTIONARY = new Matcher<String>() {
        @Override
        public boolean matches(@Nonnull ItemStack stack, String string) {
            return InventoryHelper.isOreName(stack, string);
        }
    };

    public static final Matcher<ItemStack> ITEM_STACK = new Matcher<ItemStack>() {
        @Override
        public boolean matches(@Nonnull ItemStack stack, ItemStack stack2) {
            return stack.isItemEqual(stack2);
        }
    };

    public static final Matcher<Item> ITEM = new Matcher<Item>() {
        @Override
        public boolean matches(@Nonnull ItemStack stack, Item item) {
            return stack.getItem() == item;
        }
    };

    public static final Matcher<SearchType> SPECIAL = new Matcher<SearchType>() {
        @Override
        public boolean matches(@Nonnull ItemStack stack, SearchType type) {
            if (type.equals(FLOWER)) {
                if (stack.getItem() == Item.getItemFromBlock(Blocks.RED_FLOWER) || stack.getItem() == Item.getItemFromBlock(Blocks.YELLOW_FLOWER))
                    return true;
                else {
                    for (String name : getOreNames(stack)) {
                        if (name.startsWith("flower")) return true;
                    }

                    return false;
                }
            } else if (type.equals(HOE)) {
                return stack.getItem() instanceof ItemHoe;
            } else if (type.equals(BUCKET)) {
                return stack.getItem() instanceof ItemBucket;
            } else if (type.equals(SHEARS)) {
                return stack.getItem() instanceof ItemShears;
            } else if (type.equals(WOOL)) {
                return ToolHelper.isWool(stack);
            } else if (type.equals(PICKAXE)) {
                return stack.getItem() instanceof ItemPickaxe;
            }

            return false;
        }
    };

    private static <T> void takeItems(EntityPlayer player, T taking, int amount, Matcher<T> matcher) {
        int toTake = amount;
        ItemStack offhand = player.inventory.offHandInventory.get(0);
        if (matcher.matches(offhand, taking)) {
            ItemStack taken = offhand.splitStack(toTake);
            toTake -= taken.getCount();
            player.inventory.offHandInventory.get(0).isEmpty(); //Clear
            if (toTake <= 0) return; //No further processing neccessary
        }

        //Main Inventory
        for (int i = 0; i < player.inventory.mainInventory.size() && toTake > 0; i++) {
            ItemStack stack = player.inventory.mainInventory.get(i);
            if (!stack.isEmpty() && matcher.matches(stack, taking)) {
                ItemStack taken = stack.splitStack(toTake);
                toTake -= taken.getCount();
                player.inventory.mainInventory.get(i).isEmpty(); //Clear
                if (toTake <= 0) return; //No further processing neccessary
            }
        }
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
    private static <S> int getStackSizeOfHand(EntityPlayer player, Matcher<S> matcher, S search, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (!held.isEmpty() && matcher.matches(held, search)) {
            return held.getCount();
        } else return 0;
    }

    @SuppressWarnings("unchecked")
    public static <S> EnumHand getHandItemIsIn(EntityPlayer player, Matcher<S> matcher, S search, int... amount) {
        int count = amount == null || amount.length == 0 ? 1 : amount[0];
        for (EnumHand hand : EnumHand.values()) {
            if (getStackSizeOfHand(player, matcher, search, hand) != 0) {
                if (getCount(player, search, matcher) >= count) {
                    return hand;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("ConstantConditions")
    private static int reduceHeld(EntityPlayer player, EnumHand hand, int amount) {
        ItemStack held = player.getHeldItem(hand);
        if (held.getCount() <= amount) {
            int ret = held.getCount();
            player.setHeldItem(hand, ItemStack.EMPTY);
            return ret;
        } else {
            held.shrink(amount);
            return amount;
        }
    }

    @SuppressWarnings("unchecked")
    public static <S> EnumHand takeItemsIfHeld(EntityPlayer player, Matcher matcher, S search, int... amount) {
        int count = amount == null || amount.length == 0 ? 1 : amount[0];
        EnumHand ret = getHandItemIsIn(player, matcher, search, count);
        if (ret != null) {
            count -= reduceHeld(player, ret, count); //Update the count
            if (count > 0) takeItems(player, search, count, matcher);
            return ret;
        }

        return null;
    }

    public static <T> int getCount(EntityPlayer player, T taking, Matcher<T> matcher) {
        int count = 0;
        for (ItemStack item : player.inventory.mainInventory) {
            if (item == null) continue;
            if (matcher.matches(item, taking)) {
                count += item.getCount();
            }
        }

        ItemStack offhand = player.inventory.offHandInventory.get(0);
        if (!offhand.isEmpty() && matcher.matches(offhand, taking)) {
            count += offhand.getCount();
        }

        return count;
    }

    public static boolean isOreName(@Nonnull ItemStack stack, String... ore) {
        for (String name : ore) {
            if (isOreName(stack, name)) return true;
        }

        return false;
    }

    public static boolean isOreName(@Nonnull ItemStack stack, String ore) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            String name = OreDictionary.getOreName(i);
            if (name.equals(ore)) {
                return true;
            }
        }

        return false;
    }

    public static NonNullList<ItemStack> getStarts(String ore) {
        NonNullList<ItemStack> list = NonNullList.create();
        for (String name : OreDictionary.getOreNames()) {
            if (name.startsWith(ore)) list.addAll(OreDictionary.getOres(name));
        }

        return list;
    }

    public static NonNullList<ItemStack> getEnds(String ore) {
        NonNullList<ItemStack> list = NonNullList.create();
        for (String name : OreDictionary.getOreNames()) {
            if (name.endsWith(ore)) list.addAll(OreDictionary.getOres(name));
        }

        return list;
    }

    public static NonNullList<ItemStack> getContains(String ore) {
        NonNullList<ItemStack> list = NonNullList.create();
        for (String name : OreDictionary.getOreNames()) {
            if (name.contains(ore)) list.addAll(OreDictionary.getOres(name));
        }

        return list;
    }

    public static boolean startsWith(@Nonnull ItemStack stack, String... ore) {
        for (String name : ore) {
            if (startsWith(stack, name)) return true;
        }

        return false;
    }

    public static boolean endsWith(@Nonnull ItemStack stack, String ore) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            String name = OreDictionary.getOreName(i);
            if (name.endsWith(ore)) {
                return true;
            }
        }

        return false;
    }

    public static boolean contains(@Nonnull ItemStack stack, String ore) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            String name = OreDictionary.getOreName(i);
            if (name.contains(ore)) {
                return true;
            }
        }

        return false;
    }

    public static boolean startsWith(@Nonnull ItemStack stack, String ore) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i : ids) {
            String name = OreDictionary.getOreName(i);
            if (name.startsWith(ore)) {
                return true;
            }
        }

        return false;
    }

    public static String[] getOreNames(@Nonnull ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        String[] names = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            names[i] = OreDictionary.getOreName(ids[i]);
        }

        return names;
    }

    public abstract static class Matcher<T> {
        public abstract boolean matches(@Nonnull ItemStack stack, T t);


        @SafeVarargs
        public final boolean matchesAny(@Nonnull ItemStack stack, T... t) {
            for (T value : t) {
                if (matches(stack, value)) return true;
            }

            return false;
        }
    }
}