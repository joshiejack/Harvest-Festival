package uk.joshiejack.penguinlib.data.custom.block;

import uk.joshiejack.penguinlib.block.custom.BlockMultiCustomInventory;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("block:inventory")
public class CustomBlockInventoryData extends AbstractCustomBlockData {
    public int inventorySize;

    @Nonnull
    @Override
    public Block build(ResourceLocation registryName, @Nonnull AbstractCustomBlockData main, @Nullable AbstractCustomBlockData[] sub) {
        return new BlockMultiCustomInventory(registryName, (CustomBlockInventoryData) main, (CustomBlockInventoryData[]) sub);
    }
}
