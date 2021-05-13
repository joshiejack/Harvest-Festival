package uk.joshiejack.penguinlib.data.custom.block;

import uk.joshiejack.penguinlib.block.custom.BlockMultiCustomInventoryRotatable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("block:inventory_rotatable")
public class CustomBlockInventoryRotatableData extends CustomBlockInventoryData {
    @Nonnull
    @Override
    public Block build(ResourceLocation registryName, @Nonnull AbstractCustomBlockData main, @Nullable AbstractCustomBlockData[] sub) {
        return new BlockMultiCustomInventoryRotatable(registryName, (CustomBlockInventoryData) main, (CustomBlockInventoryData[]) sub);
    }
}
