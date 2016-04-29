package joshie.harvest.items;

import joshie.harvest.core.HFTab;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class ItemHammer extends ItemBaseTool {
    public ItemHammer() {
        setCreativeTab(HFTab.MINING);
    }

    //Might want to use this for "charging" the hammer up to destroy rocks
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
}