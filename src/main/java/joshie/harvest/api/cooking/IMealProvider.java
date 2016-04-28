package joshie.harvest.api.cooking;

import net.minecraft.item.ItemStack;

public interface IMealProvider {
    /** Returns the unlocalised name of the meal this item provides 
     * @param stack **/
    public String getMealName(ItemStack stack);
}