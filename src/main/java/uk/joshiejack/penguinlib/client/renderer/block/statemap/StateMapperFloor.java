package uk.joshiejack.penguinlib.client.renderer.block.statemap;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;

import javax.annotation.Nonnull;
import java.util.Map;

public class StateMapperFloor extends DefaultStateMapper {
    private static final String[] VALID_STATES = new String[]{
            "ne=blank,nw=blank,se=blank,sw=blank",
            "ne=blank,nw=blank,se=blank,sw=inner",
            "ne=blank,nw=blank,se=horizontal,sw=horizontal",
            "ne=blank,nw=blank,se=inner,sw=blank",
            "ne=blank,nw=blank,se=inner,sw=inner",
            "ne=blank,nw=inner,se=blank,sw=blank",
            "ne=blank,nw=inner,se=blank,sw=inner",
            "ne=blank,nw=inner,se=horizontal,sw=horizontal",
            "ne=blank,nw=inner,se=inner,sw=blank",
            "ne=blank,nw=inner,se=inner,sw=inner",
            "ne=blank,nw=vertical,se=blank,sw=vertical",
            "ne=blank,nw=vertical,se=horizontal,sw=outer",
            "ne=blank,nw=vertical,se=inner,sw=vertical",
            "ne=horizontal,nw=horizontal,se=blank,sw=blank",
            "ne=horizontal,nw=horizontal,se=blank,sw=inner",
            "ne=horizontal,nw=horizontal,se=horizontal,sw=horizontal",
            "ne=horizontal,nw=horizontal,se=inner,sw=blank",
            "ne=horizontal,nw=horizontal,se=inner,sw=inner",
            "ne=horizontal,nw=outer,se=blank,sw=vertical",
            "ne=horizontal,nw=outer,se=horizontal,sw=outer",
            "ne=horizontal,nw=outer,se=inner,sw=vertical",
            "ne=inner,nw=blank,se=blank,sw=blank",
            "ne=inner,nw=blank,se=blank,sw=inner",
            "ne=inner,nw=blank,se=horizontal,sw=horizontal",
            "ne=inner,nw=blank,se=inner,sw=blank",
            "ne=inner,nw=blank,se=inner,sw=inner",
            "ne=inner,nw=inner,se=blank,sw=blank",
            "ne=inner,nw=inner,se=blank,sw=inner",
            "ne=inner,nw=inner,se=horizontal,sw=horizontal",
            "ne=inner,nw=inner,se=inner,sw=blank",
            "ne=inner,nw=inner,se=inner,sw=inner",
            "ne=inner,nw=vertical,se=blank,sw=vertical",
            "ne=inner,nw=vertical,se=horizontal,sw=outer",
            "ne=inner,nw=vertical,se=inner,sw=vertical",
            "ne=outer,nw=horizontal,se=outer,sw=horizontal",
            "ne=outer,nw=horizontal,se=vertical,sw=blank",
            "ne=outer,nw=horizontal,se=vertical,sw=inner",
            "ne=outer,nw=outer,se=outer,sw=outer",
            "ne=outer,nw=outer,se=vertical,sw=vertical",
            "ne=vertical,nw=blank,se=outer,sw=horizontal",
            "ne=vertical,nw=blank,se=vertical,sw=blank",
            "ne=vertical,nw=blank,se=vertical,sw=inner",
            "ne=vertical,nw=inner,se=outer,sw=horizontal",
            "ne=vertical,nw=inner,se=vertical,sw=blank",
            "ne=vertical,nw=inner,se=vertical,sw=inner",
            "ne=vertical,nw=vertical,se=outer,sw=outer",
            "ne=vertical,nw=vertical,se=vertical,sw=vertical"
    };

    private boolean isValidState(String state) {
        for (String s : VALID_STATES) {
            if (state.equalsIgnoreCase(s)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block block) {
        mapStateModelLocations.put(new BlockStateContainer(block).getBaseState(), new ModelResourceLocation(block.getRegistryName(), "overlay")); //Register the overlay model

        //State > Resource
        for (IBlockState iblockstate : block.getBlockState().getValidStates()) {
            ModelResourceLocation model = getModelResourceLocation(iblockstate);
            if (model != null) {
                mapStateModelLocations.put(iblockstate, model);
            }
        }

        return mapStateModelLocations;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        String properties = this.getPropertyString(state.getProperties());
        if (!isValidState(properties)) return null;
        return new ModelResourceLocation(state.getBlock().getRegistryName(), properties);
    }
}
