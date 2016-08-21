package joshie.harvest.npc.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.util.holders.HolderRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GiftRegistry implements IGiftRegistry {
    private static final HolderRegistry<GiftCategory[]> registry = new HolderRegistry<GiftCategory[]>() {
        @Override
        public boolean matches(GiftCategory[] external, GiftCategory[] internal) {
            for (GiftCategory e: external) {
                for (GiftCategory i: internal) {
                    if (e == i) return true;
                }
            }

            return false;
        }
    };

    @Override
    public void assignStack(ItemStack stack, GiftCategory... categories) {
        registry.registerItem(stack, categories);
    }

    @Override
    public void assignMod(String mod, GiftCategory... categories) {
        registry.registerMod(mod, categories);
    }

    @Override
    public boolean isGiftType(ItemStack stack, GiftCategory... categories) {
        return registry.matches(stack, categories);
    }

    //For my own sanity....
    public void assign(Object object, GiftCategory... category) {
        if (object instanceof Item) assignItem((Item)object, category);
        else if (object instanceof Block) assignBlock((Block)object, category);
        else if (object instanceof ItemStack) assignStack((ItemStack)object, category);
    }

    private void assignItem(Item item, GiftCategory... categories) {
        registry.registerItem(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), categories);
    }

    private void assignBlock(Block block, GiftCategory... categories) {
        registry.registerItem(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), categories);
    }
}
