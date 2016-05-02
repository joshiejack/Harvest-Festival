package joshie.harvest.crops;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class CropState extends BlockStateContainer {
    private ImmutableSortedMap< String, IProperty<? >> cropProperties;
    private static final Function< IProperty<?>, String > GET_NAME_FUNC = new Function < IProperty<?>, String > () {
        public String apply(IProperty<?> apply) {
            return apply == null ? "<NULL>" : apply.getName();
        }
    };

    private final String crop;

    public CropState(String crop, IProperty<?>... properties) {
        super(HFBlocks.CROPS, properties);
        this.crop = crop;
        cropProperties = ReflectionHelper.getPrivateValue(BlockStateContainer.class, this, "properties");
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("block", Block.REGISTRY.getNameForObject(HFBlocks.CROPS) + "_" + crop).add("properties", Iterables.transform(this.cropProperties.values(), GET_NAME_FUNC)).toString();
    }
}