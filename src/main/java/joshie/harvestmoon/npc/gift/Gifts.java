package joshie.harvestmoon.npc.gift;

import java.util.HashMap;

import joshie.harvestmoon.util.SafeStack;
import joshie.harvestmoon.util.WildStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class Gifts {

    private static HashMap<SafeStack, Category[]> gifts = new HashMap();;

    public Quality getQuality(ItemStack stack) {
        return Quality.DECENT;
    }

    public boolean is(ItemStack stack, Category category) {
        SafeStack safe = new SafeStack(stack);
        Category[] categories = gifts.get(safe);
        if (categories == null || categories.length < 1) return false;
        for (Category cat : categories) {
            if (cat == category) return true;
        }

        return false;
    }

    public static void assign(Object item, Category... category) {
        if (item instanceof Item) {
            gifts.put(new WildStack(new ItemStack((Item) item)), category);
        } else if (item instanceof Block) {
            gifts.put(new WildStack(new ItemStack((Block) item)), category);
        } else {
            gifts.put(new SafeStack((ItemStack) item), category);
        }
    }

    public static enum Category {
        CUTE, SCARY, MINING, NATURE, ANIMALS, COOKING, TOOLS, WATER, PRETTY, CHEAP, FARMING, CONSTRUCTION, RARE, GIRLY, KNITTING, TECHNOLOGY, DANGER, BATTLE;
    }

    public static enum Quality {
        AWESOME(800), GOOD(500), DECENT(300), DISLIKE(-500), BAD(-800), TERRIBLE(-5000);

        private final int relationpoints;

        private Quality(int relationpoints) {
            this.relationpoints = relationpoints;
        }

        public int getRelationPoints() {
            return relationpoints;
        }
    }
}
