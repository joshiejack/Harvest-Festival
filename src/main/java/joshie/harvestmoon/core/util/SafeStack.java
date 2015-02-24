package joshie.harvestmoon.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import scala.Immutable;

public class SafeStack implements Immutable {
    public String item;
    public int damage;

    public SafeStack(String item, int damage) {
        this.item = item;
        this.damage = damage;
    }
    
    public SafeStack(ItemStack stack) {
        this.item = Item.itemRegistry.getNameForObject(stack.getItem());
        this.damage = stack.getItemDamage();
    }
    
    public ItemStack getItem() {
        return new ItemStack((Item)Item.itemRegistry.getObject(item), 1, damage);
    }

    @Override
    public boolean equals(Object obj) {        
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SafeStack other = (SafeStack) obj;
        if(damage != OreDictionary.WILDCARD_VALUE && damage != other.damage)
            return false;
        if (item == null) {
            if (other.item != null)
                return false;
        } else if (!item.equals(other.item))
            return false;
        return true;
    }
    
    @Override
    public int hashCode() {        
        final int prime = 31;
        int result = 1;
        result = prime * result + damage;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        return result;
    }
}
