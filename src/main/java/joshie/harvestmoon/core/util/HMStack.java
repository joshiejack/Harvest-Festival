package joshie.harvestmoon.core.util;

import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.helpers.SizeableHelper;
import joshie.harvestmoon.core.lib.SizeableMeta;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;

public class HMStack extends SafeStack {
    private Crop crop;
    private SizeableMeta sized;

    public HMStack(ItemStack stack) {
        super(stack);

        if (stack.getItem() == HMItems.crops) {
            crop = CropHelper.getCropFromStack(stack);
        } else if (stack.getItem() == HMItems.sized) {
            sized = SizeableHelper.getSizeableFromStack(stack);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HMStack other = (HMStack) obj;
        if (item == null || other.item == null) return false;
        if (!item.equals(other.item)) return false;
        if (crop != null && other.crop == null) return false;
        if (sized != null && other.sized == null) return false;
        if (crop != null) return crop.equals(obj);
        if (sized != null) return sized.equals(obj);
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        result = prime * result + ((crop == null) ? 0 : crop.hashCode());
        result = prime * result + ((sized == null) ? 0 : sized.ordinal());
        return result;
    }
}
