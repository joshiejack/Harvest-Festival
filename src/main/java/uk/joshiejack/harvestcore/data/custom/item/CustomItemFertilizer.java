package uk.joshiejack.harvestcore.data.custom.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.harvestcore.item.custom.ItemFertilizer;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@PenguinLoader(value = "item:fertilizer")
public class CustomItemFertilizer extends AbstractCustomData.ItemOrBlock<ItemFertilizer, CustomItemFertilizer> {
    private transient Fertilizer fertilizer;
    public int speed;
    public int quality;

    @Nonnull
    @Override
    public ItemFertilizer build(ResourceLocation registryName, @Nonnull CustomItemFertilizer main, @Nullable CustomItemFertilizer... data) {
        return new ItemFertilizer(registryName, main, data);
    }

    @Override
    public void init(ItemStack stack) {
        super.init(stack);
        String modid = Objects.requireNonNull(stack.getItem().getRegistryName()).getNamespace();
        fertilizer = Fertilizer.create(new ResourceLocation(modid, name), speed, quality);
    }

    public Fertilizer getFertilizer() {
        return fertilizer;
    }
}
