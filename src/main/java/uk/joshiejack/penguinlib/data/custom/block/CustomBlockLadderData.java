package uk.joshiejack.penguinlib.data.custom.block;

import uk.joshiejack.penguinlib.block.custom.BlockCustomLadder;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("block:ladder")
public class CustomBlockLadderData extends AbstractCustomBlockData {
    @Nonnull
    @Override
    public Block build(ResourceLocation registryName, @Nonnull AbstractCustomBlockData main, @Nullable AbstractCustomBlockData[] sub) {
        return new BlockCustomLadder(registryName, main, sub);
    }
}
