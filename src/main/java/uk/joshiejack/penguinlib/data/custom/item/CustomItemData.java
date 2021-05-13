package uk.joshiejack.penguinlib.data.custom.item;

import uk.joshiejack.penguinlib.item.custom.ItemCustom;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:singular")
public class CustomItemData extends AbstractItemData<ItemCustom, CustomItemData> {
    @Nonnull
    @Override
    public ItemCustom build(ResourceLocation registryName, @Nonnull CustomItemData main, @Nullable CustomItemData... sub) {
        return new ItemCustom(registryName, main == null ? new CustomItemData() : main);
    }
}
