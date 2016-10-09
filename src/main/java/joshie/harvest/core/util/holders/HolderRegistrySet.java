package joshie.harvest.core.util.holders;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.core.Mod;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.item.ItemSizeable;
import joshie.harvest.core.util.interfaces.IFMLItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

public class HolderRegistrySet {
    private final Multimap<Item, AbstractItemHolder> setMap = HashMultimap.create();

    private void registerHolder(Item item, AbstractItemHolder holder) {
        setMap.get(item).add(holder);
    }

    public void register(Object object) {
        if (object instanceof Item) registerHolder((Item)object, ItemHolder.of((Item)object));
        if (object instanceof Block) {
            ItemStack stack = new ItemStack((Block)object);
            registerHolder(stack.getItem(), ItemHolder.of(stack.getItem()));
        } else if (object instanceof ItemStack) {
            registerHolder(((ItemStack)object).getItem(), getHolder((ItemStack)object));
        } else if (object instanceof Mod) {
            Mod mod = (Mod) object;
            ModHolder holder = ModHolder.of(mod.getMod());
            for (Item item: Item.REGISTRY) {
                if (item.getRegistryName().getResourceDomain().equals(mod.getMod())) {
                    registerHolder(item, holder);
                }
            }
        } else if (object instanceof Ore) {
            Ore ore = (Ore) object;
            OreHolder holder = OreHolder.of(ore.getOre());
            for (ItemStack stack: OreDictionary.getOres(ore.getOre())) {
                registerHolder(stack.getItem(), holder);
            }
        }
    }

    public boolean contains(ItemStack stack) {
        Collection<AbstractItemHolder> holders = setMap.get(stack.getItem());
        for (AbstractItemHolder holder: holders) {
            if (holder.matches(stack)) {
                return true;
            }
        }

        return false;
    }

    private AbstractItemHolder getHolder(ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return ItemHolder.of(stack.getItem());
        else if (stack.getItem() instanceof ItemSizeable) return SizeableHolder.of(HFCore.SIZEABLE.getObjectFromStack(stack));
        else if (stack.getItem() instanceof ICropProvider) return CropHolder.of(((ICropProvider)stack.getItem()).getCrop(stack));
        else if (stack.getItem() instanceof IFMLItem) return FMLHolder.of(stack);
        else return ItemStackHolder.of(stack);
    }
}
