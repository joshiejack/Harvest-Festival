package uk.joshiejack.penguinlib.data.custom.item;

import uk.joshiejack.penguinlib.item.custom.ItemMultiCustom;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:multi")
public class CustomItemMultiData extends AbstractItemData<ItemMultiCustom<?>, CustomItemMultiData> {
    @Nonnull
    @Override
    public ItemMultiCustom<?> build(ResourceLocation registryName, @Nonnull CustomItemMultiData main, @Nullable CustomItemMultiData... sub) {
        return new ItemMultiCustom<>(registryName, main, sub);
    }
}
