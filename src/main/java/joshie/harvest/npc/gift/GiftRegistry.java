package joshie.harvest.npc.gift;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.player.tracking.TrackingData.AbstractItemHolder;
import joshie.harvest.player.tracking.TrackingData.ItemHolder;
import joshie.harvest.player.tracking.TrackingData.ItemStackHolder;
import joshie.harvest.player.tracking.TrackingData.ModHolder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class GiftRegistry implements IGiftRegistry {
    private static final HashMap<AbstractItemHolder, GiftCategory[]> GIFTS = new HashMap<>();
    private static final HashMultimap<Item, AbstractItemHolder> KEY_MAP = HashMultimap.create();

    public static void assignItem(Item item, GiftCategory... categories) {
        ItemHolder holder = new ItemHolder(item);
        KEY_MAP.get(item).add(holder);
        GIFTS.put(holder, categories);
    }

    public static void assignBlock(Block block, GiftCategory... categories) {
        assignItem(Item.getItemFromBlock(block), categories);
    }

    public static void assignStack(ItemStack stack, GiftCategory... categories) {
        ItemStackHolder holder = new ItemStackHolder(stack);
        KEY_MAP.get(stack.getItem()).add(holder);
        GIFTS.put(holder, categories);
    }

    public static void assign(Object object, GiftCategory... category) {
        if (object instanceof Item) assignItem((Item)object, category);
        else if (object instanceof Block) assignBlock((Block)object, category);
        else if (object instanceof ItemStack) assignStack((ItemStack)object, category);
    }

    public static void assignModID(String mod, GiftCategory... categories) {
        ModHolder holder = new ModHolder(mod);
        for (Item item: Item.REGISTRY) {
            if (item.getRegistryName().getResourceDomain().equals(mod)) {
                KEY_MAP.get(item).add(holder);
            }
        }

        GIFTS.put(holder, categories);
    }

    public static boolean is(ItemStack stack, GiftCategory category) {
        for (AbstractItemHolder holder: KEY_MAP.get(stack.getItem())) {
            if (holder.matches(stack)) {
                GiftCategory[] categories = GIFTS.get(holder);
                if (categories == null || categories.length < 1) continue;
                for (GiftCategory cat: categories) {
                    if (cat == category) return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isGiftType(GiftCategory category, ItemStack stack) {
        return is(stack, category);
    }
}
