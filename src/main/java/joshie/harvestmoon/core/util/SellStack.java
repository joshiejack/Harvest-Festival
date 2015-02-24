package joshie.harvestmoon.core.util;

import net.minecraft.item.ItemStack;

public class SellStack extends SafeStack {
    public int amount;
    public long sell;

    public SellStack(String item, int damage, int amount, long sell) {
        super(item, damage);
        this.amount = amount;
        this.sell = sell;
    }
    
    public SellStack(ItemStack stack, long sell) {
        super(stack);
        this.sell = sell;
    }
    
    public void add(long sell) {
        this.sell += sell;
        amount++;
    }
}
