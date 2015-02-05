package joshie.harvestmoon.util;

import net.minecraft.item.ItemStack;

public class WildStack extends SafeStack {
    public WildStack(ItemStack stack) {
        super(stack);
    }
    
    @Override
    public boolean equals(Object obj) {        
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WildStack other = (WildStack) obj;
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
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        return result;
    }
}
