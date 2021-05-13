package uk.joshiejack.penguinlib.data.custom.block;

import uk.joshiejack.penguinlib.block.custom.BlockCustomFluid;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("block:fluid")
public class CustomBlockFluidData extends AbstractCustomBlockData {
    private ResourceLocation still;
    private ResourceLocation flow;

    @Nonnull
    @Override
    public Block build(ResourceLocation registryName, @Nonnull AbstractCustomBlockData main, @Nullable AbstractCustomBlockData[] sub) {
        CustomBlockFluidData fluidData = (CustomBlockFluidData) main;
        Fluid fluid = RegistryHelper.createFluid(registryName.getPath(), fluidData.still, fluidData.flow);
        return new BlockCustomFluid(fluid, registryName.getNamespace(), main.getScript());
    }
}
