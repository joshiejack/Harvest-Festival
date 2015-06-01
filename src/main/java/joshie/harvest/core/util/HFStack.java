package joshie.harvest.core.util;

import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import net.minecraft.item.ItemStack;

public class HFStack extends SafeStack {
    private ICrop crop;
    private ISizeable sized;

    public HFStack(ItemStack stack) {
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
        HFStack other = (HFStack) obj;
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
