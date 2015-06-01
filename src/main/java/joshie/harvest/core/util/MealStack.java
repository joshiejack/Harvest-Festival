package joshie.harvest.core.util;

import joshie.harvest.api.cooking.IMealProvider;
import net.minecraft.item.ItemStack;

public class MealStack extends SafeStack {
    public String mealname;
    
    public MealStack(ItemStack stack) {
        super(stack);
        if (stack.getItem() instanceof IMealProvider) {
            mealname = ((IMealProvider)(stack.getItem())).getMealName(stack);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime * ((mealname == null) ? 0 : mealname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        MealStack other = (MealStack) obj;
        if (mealname == null || other.mealname == null) {
            return false;
        } else if (!mealname.equals(other.mealname)) return false;
        return true;
    }
}
