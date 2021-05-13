package uk.joshiejack.penguinlib.item;

import uk.joshiejack.penguinlib.PenguinConfig;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;


public class ItemDinnerware extends ItemMulti<ItemDinnerware.Dinnerware> {
    public ItemDinnerware() {
        super(new ResourceLocation(MOD_ID, "dinnerware"), Dinnerware.class);
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @Nonnull
    @Override
    protected ItemStack getCreativeStack(Dinnerware object) {
        return PenguinConfig.requireDishes ? super.getCreativeStack(object) : ItemStack.EMPTY;
    }

    public enum Dinnerware implements IStringSerializable {
        BOWL, PICKLING_JAR, GLASS, PLATE_UNFIRED, PLATE_FIRED, MUG_UNFIRED, MUG_FIRED, JAM_JAR;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
