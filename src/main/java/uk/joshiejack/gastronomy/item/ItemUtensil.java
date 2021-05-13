package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemUtensil extends ItemMulti<ItemUtensil.Utensil> {
    public ItemUtensil() {
        super(new ResourceLocation(MODID, "utensil"), Utensil.class);
        setCreativeTab(Gastronomy.TAB);
        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(Utensil utensil) {
        return utensil == Utensil.KNIFE ? super.getCreativeStack(utensil) : ItemStack.EMPTY;
    }

    public enum Utensil {
        KNIFE, BLADE
    }
 }
