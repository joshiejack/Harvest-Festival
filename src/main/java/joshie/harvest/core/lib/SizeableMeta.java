package joshie.harvest.core.lib;

import joshie.harvest.api.core.ISizeable;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.init.HFConfig;
import joshie.harvest.items.ItemSized;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum SizeableMeta implements ISizeable {
    EGG(50, 60, 80, !HFConfig.vanilla.EGG_OVERRIDE), MILK(100, 150, 200), WOOL(100, 400, 500), 
    MAYONNAISE(300, 400, 500), YOGHURT(150, 200, 300), CHEESE(100, 150, 200), YARN(300, 700, 800), 
    MATSUTAKE(350, 500, 800), TOADSTOOL(100, 130, 160), SHIITAKE(50, 80, 120);

    private int small, medium, large;
    private boolean enabled;
    
    private SizeableMeta(int small, int medium, int large, boolean enabled) {
        this(small, medium, large);
        this.enabled = enabled;
    }

    private SizeableMeta(int small, int medium, int large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.enabled = true;
    }

    public int getSellValue(Size size) {
        if (size == Size.LARGE) return large;
        else if (size == Size.MEDIUM) return medium;
        else return small;
    }
    
    public boolean isVanilla() {
        return !enabled;
    }

    @Override
    public Size getSize(ItemStack stack) {
        return SizeableHelper.getSize(stack.getItemDamage());
    }

    public Item getOrCreateStack() {
        if (this == EGG && isVanilla()) {
            return Items.egg;
        }
        
        return new ItemSized(this).setUnlocalizedName(name().toLowerCase());
    }
}
