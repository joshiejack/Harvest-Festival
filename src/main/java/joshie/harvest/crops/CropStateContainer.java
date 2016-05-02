package joshie.harvest.crops;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;

public class CropStateContainer extends BlockStateContainer {
    private static String crop;

    public CropStateContainer(String crop, Block blockIn, IProperty<?>... properties) {
        super(block(crop, blockIn), properties);
    }

    private static Block block(String crop, Block block) {
        CropStateContainer.crop = crop;
        return block;
    }

    protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<net.minecraftforge.common.property.IUnlistedProperty<?>, com.google.common.base.Optional<?>> unlistedProperties) {
        return new CropState(new String(crop), block, properties);
    }
}