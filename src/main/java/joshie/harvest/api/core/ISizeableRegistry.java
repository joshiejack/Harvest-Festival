package joshie.harvest.api.core;

import joshie.harvest.api.core.ISizeable.Size;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

/** This is for registering sizeable types **/
public interface ISizeableRegistry {
    /** Create a new sizeable item, if you don't wish to use your own items **/
    Item createSizedItem(String name, long sellSmall, long sellMedium, long sellLarge);

    /** Register a new sizeable handler, this is for storing different sell values **/
    ISizeable registerSizeable(ResourceLocation key, long sellSmall, long sellMedium, long sellLarge);

    /** Register an item as a sizeable, of a specific size **/
    ISizeable registerSizeableProvider(ItemStack stack, ISizeable sizeable, Size size);

    /** Returns the sizeable type for this item **/
    Pair<ISizeable, Size> getSizeableFromStack(ItemStack stack);
}
