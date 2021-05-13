package uk.joshiejack.penguinlib.item.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class ItemSpecial extends ItemMulti<ItemSpecial.Special> {
    public ItemSpecial() {
        super(new ResourceLocation(MOD_ID, "special"), Special.class);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {}

    public enum Special {
        SPEECH_BUBBLE, MAIL
    }
}
