package joshie.harvestmoon.core.lib;

import joshie.harvestmoon.api.core.ISizeable;
import joshie.harvestmoon.core.helpers.SizeableHelper;
import joshie.harvestmoon.init.HMConfiguration;
import net.minecraft.item.ItemStack;

public enum SizeableMeta implements ISizeable {
    EGG(50, 60, 80, !HMConfiguration.vanilla.EGG_OVERRIDE), MILK(100, 150, 200), WOOL(100, 400, 500), 
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
}
