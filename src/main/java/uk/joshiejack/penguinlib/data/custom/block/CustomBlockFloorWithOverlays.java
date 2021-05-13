package uk.joshiejack.penguinlib.data.custom.block;

import uk.joshiejack.penguinlib.block.custom.BlockCustomFloorWithOverlays;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("block:overlayed_floor")
public class CustomBlockFloorWithOverlays extends AbstractCustomBlockData {
    public FloorOverlay[] overlays;

    @Nonnull
    @Override
    public Block build(ResourceLocation registryName, @Nonnull AbstractCustomBlockData main, @Nullable AbstractCustomBlockData[] sub) {
        return new BlockCustomFloorWithOverlays(registryName, (CustomBlockFloorWithOverlays) main);
    }

    public static class FloorOverlay {
        private int weight;
        private ResourceLocation texture;

        public int weight() {
            return weight;
        }

        public ResourceLocation texture() {
            return texture;
        }
    }
}
