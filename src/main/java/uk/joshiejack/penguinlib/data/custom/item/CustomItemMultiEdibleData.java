package uk.joshiejack.penguinlib.data.custom.item;

import joptsimple.internal.Strings;
import uk.joshiejack.penguinlib.item.custom.ItemMultiEdibleCustom;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:edible")
public class CustomItemMultiEdibleData extends AbstractItemData<ItemMultiEdibleCustom, CustomItemMultiEdibleData> {
    public boolean alwaysEdible = false;
    public EnumAction action = null;
    public int hunger = -1;
    public float saturation = -1F;
    public int consumeTime = -1;
    public String leftovers;

    @Nonnull
    @Override
    public ItemMultiEdibleCustom build(ResourceLocation registryName, @Nonnull CustomItemMultiEdibleData main, @Nullable CustomItemMultiEdibleData... sub) {
        return new ItemMultiEdibleCustom(registryName, main, sub);
    }

    public ItemStack getLeftovers() {
        return Strings.isNullOrEmpty(leftovers) ? ItemStack.EMPTY : StackHelper.getStackFromString(leftovers);
    }
}
