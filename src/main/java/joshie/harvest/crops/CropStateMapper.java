package joshie.harvest.crops;

import com.google.common.collect.Maps;
import joshie.harvest.api.crops.Crop;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

import javax.annotation.Nonnull;
import java.util.Map;

public class CropStateMapper extends StateMapperBase {
    @Override
    @Nonnull
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
        Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP && !crop.skipLoadingRender()).forEachOrdered(crop -> {
            for (Object object : crop.getStateHandler().getValidStates()) {
                IBlockState state = (IBlockState) object;
                mapStateModelLocations.put(state, getCropResourceLocation(crop, state));
            }
        });

        return mapStateModelLocations;
    }

    @Override
    @Nonnull
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(HFCrops.CROPS.property); //Remove the base property for rendering purposes
        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
    }

    private ModelResourceLocation getCropResourceLocation(Crop crop, IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(HFCrops.CROPS.property); //Remove the base property for rendering purposes
        return new ModelResourceLocation(crop.getResource().getResourceDomain() + ":crops_" + crop.getResource().getResourcePath(), this.getPropertyString(map));
    }
}