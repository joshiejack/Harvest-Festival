package joshie.harvestmoon.core.util;

import joshie.harvestmoon.api.core.ISizeable;
import joshie.harvestmoon.api.core.ISizedProvider;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropProvider;
import net.minecraft.item.ItemStack;

public class HMStack extends SafeStack {
    private ICrop crop;
    private ISizeable sized;

    public HMStack(ItemStack stack) {
        super(stack);

        if (stack.getItem() instanceof ICropProvider) {
            crop = ((ICropProvider) stack.getItem()).getCrop(stack);
        } else if (stack.getItem() instanceof ISizedProvider) {
            sized = ((ISizedProvider)stack.getItem()).getSizeable(stack);
        }
    }

    @Override
    public boolean equals(Object obj) {        
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HMStack other = (HMStack) obj;
        if (crop != null && other.crop == null) return false;
        if (sized != null && other.sized == null) return false;
        if (crop != null) return crop.equals(other.crop);
        if (sized != null) return sized.equals(other.sized);
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((crop == null) ? 0 : crop.hashCode());
        result = prime * result + ((sized == null) ? 0 : sized.hashCode());
        return result;
    }
}
