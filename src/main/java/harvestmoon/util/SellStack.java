package harvestmoon.util;

import net.minecraft.item.ItemStack;

public class SellStack extends SafeStack {
    public int amount;
    public int sell;

    public SellStack(String item, int damage, int amount, int sell) {
        super(item, damage);
        this.amount = amount;
        this.sell = sell;
    }
    
    public SellStack(ItemStack stack, int sell) {
        super(stack);
        this.sell = sell;
    }
    
    public void add(int sell) {
        this.sell += sell;
        amount++;
    }
}
