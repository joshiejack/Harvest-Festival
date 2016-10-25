package joshie.harvest.crops;

import com.google.common.collect.Maps;
import joshie.harvest.api.crops.Crop;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

import java.util.Map;

public class CropStateMapper extends StateMapperBase {
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop == Crop.NULL_CROP) continue;
            if (crop.skipLoadingRender()) continue;
            for (IBlockState state : crop.getStateHandler().getValidStates()) {
                mapStateModelLocations.put(state, getCropResourceLocation(crop, state));
            }
        }

        return mapStateModelLocations;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(HFCrops.CROPS.property); //Remove the base property for rendering purposes
        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
    }

    private ModelResourceLocation getCropResourceLocation(Crop crop, IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
        map.remove(HFCrops.CROPS.property); //Remove the base property for rendering purposes
        return new ModelResourceLocation(crop.getRegistryName().getResourceDomain() + ":crops_" + crop.getRegistryName().getResourcePath(), this.getPropertyString(map));
    }
}