package uk.joshiejack.harvestcore.data.custom.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.harvestcore.item.custom.ItemStarter;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomData;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader(value = "item:starter")
public class CustomItemStarter extends AbstractCustomData.ItemOrBlock<ItemStarter, CustomItemStarter> {
    public IBlockState state;

    @Nonnull
    @Override
    public ItemStarter build(ResourceLocation registryName, @Nonnull CustomItemStarter main, @Nullable CustomItemStarter... data) {
        return new ItemStarter(registryName, main, data);
    }

    public IBlockState getBlock() {
        return state;
    }
}
