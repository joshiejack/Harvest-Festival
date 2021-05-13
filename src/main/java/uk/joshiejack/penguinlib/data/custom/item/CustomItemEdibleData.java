package uk.joshiejack.penguinlib.data.custom.item;

import joptsimple.internal.Strings;
import uk.joshiejack.penguinlib.item.custom.ItemCustomEdible;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:edible_singular")
public class CustomItemEdibleData extends AbstractItemData<ItemCustomEdible, CustomItemEdibleData> {
    public boolean alwaysEdible = false;
    public EnumAction action = null;
    public int hunger = -1;
    public float saturation = -1F;
    public int consumeTime = -1;
    public String leftovers;

    @Nonnull
    @Override
    public ItemCustomEdible build(ResourceLocation registryName, @Nonnull CustomItemEdibleData main, @Nullable CustomItemEdibleData... sub) {
        return new ItemCustomEdible(registryName, main);
    }

    public ItemStack getLeftovers() {
        return Strings.isNullOrEmpty(leftovers) ? ItemStack.EMPTY : StackHelper.getStackFromString(leftovers);
    }
}
