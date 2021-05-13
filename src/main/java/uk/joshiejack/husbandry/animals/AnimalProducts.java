package uk.joshiejack.husbandry.animals;

import net.minecraft.item.ItemStack;

public class AnimalProducts {
    public static final AnimalProducts NONE = new AnimalProducts(Integer.MAX_VALUE, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    private final ItemStack productSmall;
    private final ItemStack productMedium;
    private final ItemStack productLarge;
    private final int dayBetween;

    public AnimalProducts (int daysBetween, ItemStack productSmall, ItemStack productMedium, ItemStack productLarge){
        this.dayBetween = daysBetween;
        this.productSmall = productSmall;
        this.productMedium = productMedium;
        this.productLarge = productLarge;
    }

    public int getDaysBetweenProducts() {
        return dayBetween;
    }

    /** Size will be 0, 1 or 2 **/
    public ItemStack getProduct(int size) {
        return size == 0 ? productSmall : size == 1 ? productMedium : productLarge;
    }
}
