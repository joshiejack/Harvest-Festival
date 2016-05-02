package joshie.harvest.crops;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer.StateImplementation;

public class CropState extends StateImplementation {
    private final String crop;
    protected CropState(String crop, Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        super(blockIn, propertiesIn);
        this.crop = crop;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}