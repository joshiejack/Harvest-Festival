package joshie.harvest.npcs.gift;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.util.holders.HolderRegistry;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import net.minecraft.item.*;

public class GiftRegistry implements IGiftRegistry {
    private final HolderRegistrySet blacklist = new HolderRegistrySet();
    private final HolderRegistry<GiftCategory[]> registry = new HolderRegistry<GiftCategory[]>() {
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

    public HolderRegistry<GiftCategory[]> getRegistry() {
        return registry;
    }

    public boolean isBlacklisted(ItemStack stack) {
        if (registry.getValueOf(stack) == null &&
                (stack.getItem() instanceof ItemBlock ||
                 stack.getItem() instanceof ItemTool ||
                 stack.getItem() instanceof ItemArmor ||
                 stack.getItem() instanceof ItemSword ||
                 stack.getItem() instanceof net.minecraft.item.ItemTool)) {

            return true;
        }

        return blacklist.contains(stack);
    }

    @Override
    public void addToBlacklist(Object... objects) {
        for (Object object: objects) blacklist.register(object);
    }

    @Override
    public void setCategories(Object object, GiftCategory... categories) {
        registry.register(object, categories);
    }

    @Override
    public boolean isGiftType(ItemStack stack, GiftCategory... categories) {
        return registry.matches(stack, categories);
    }
}
