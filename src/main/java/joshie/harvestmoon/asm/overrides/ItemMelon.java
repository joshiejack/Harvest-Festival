package joshie.harvestmoon.asm.overrides;

import net.minecraft.item.ItemFood;

public class ItemMelon extends ItemFood {
    public ItemMelon(int hunger, float saturation, boolean isMeat) {
        super(hunger, saturation, isMeat);
    }
}
