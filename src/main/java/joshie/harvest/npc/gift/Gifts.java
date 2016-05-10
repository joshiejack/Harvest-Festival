package joshie.harvest.npc.gift;

import com.google.common.collect.HashMultimap;
import joshie.harvest.player.tracking.TrackingData.GiftHolder;
import joshie.harvest.player.tracking.TrackingData.ItemHolder;
import joshie.harvest.player.tracking.TrackingData.ItemHolderStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public abstract class Gifts {
    private static HashMap<GiftHolder, Category[]> gifts = new HashMap<>();
    private static HashMultimap<Item, GiftHolder> keyMap = HashMultimap.create();

    public Quality getQuality(ItemStack stack) {
        return Quality.DECENT;
    }

    private GiftHolder getHolder(ItemStack stack) {
        for (GiftHolder holder: keyMap.get(stack.getItem())) {
            if (holder.matches(stack)) return holder;
        }

        return null;
    }

    public boolean is(ItemStack stack, Category category) {
        GiftHolder holder = getHolder(stack);
        if (holder == null) return false;
        Category[] categories = gifts.get(holder);
        if (categories == null || categories.length < 1) return false;
        for (Category cat : categories) {
            if (cat == category) return true;
        }

        return false;
    }

    public static void assignItem(Item item, Category... categories) {
        ItemHolder holder = new ItemHolder(item);
        keyMap.get(item).add(holder);
        gifts.put(holder, categories);
    }

    public static void assignBlock(Block block, Category... categories) {
        assignItem(Item.getItemFromBlock(block), categories);
    }

    public static void assignStack(ItemStack stack, Category... categories) {
        ItemHolderStack holder = new ItemHolderStack(stack);
        keyMap.get(stack.getItem()).add(holder);
        gifts.put(holder, categories);
    }

    public static void assign(Object object, Category... category) {
        if (object instanceof Item) assignItem((Item)object, category);
        else if (object instanceof Block) assignBlock((Block)object, category);
        else if (object instanceof ItemStack) assignStack((ItemStack)object, category);
    }

    public enum Category {
        CUTE, SCARY, MINING, NATURE, ANIMALS, COOKING, TOOLS, WATERY, PRETTY, CHEAP, FARMING, CONSTRUCTION, RARE, GIRLY, KNITTING, TECHNOLOGY, DANGER, BATTLE;
    }

    public enum Quality {
        AWESOME(800), GOOD(500), DECENT(300), DISLIKE(-500), BAD(-800), TERRIBLE(-5000);

        private final int points;

        Quality(int points) {
            this.points = points;
        }

        public int getRelationPoints() {
            return points;
        }
    }
}