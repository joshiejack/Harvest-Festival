package joshie.harvest.cooking.items;

import joshie.harvest.api.cooking.IKnife;
import joshie.harvest.cooking.items.ItemUtensil.Utensil;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.ItemHFEnum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import static joshie.harvest.cooking.items.ItemUtensil.Utensil.KNIFE;

public class ItemUtensil extends ItemHFEnum<ItemUtensil, Utensil> implements IKnife {
    public enum Utensil implements IStringSerializable {
        BLADE, KNIFE;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemUtensil() {
        super(HFTab.COOKING, Utensil.class);
    }

    @Override
    public boolean isKnife(ItemStack stack) {
        return getEnumFromStack(stack) == KNIFE;
    }

    @Override
    public boolean shouldDisplayInCreative(Utensil ingredient) {
        return ingredient == KNIFE;
    }
}
