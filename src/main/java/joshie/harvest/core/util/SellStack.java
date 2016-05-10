package joshie.harvest.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SellStack extends SafeStack {
    public int amount;
    public long sell;

    public SellStack(Item item, int damage, int amount, long sell) {
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
