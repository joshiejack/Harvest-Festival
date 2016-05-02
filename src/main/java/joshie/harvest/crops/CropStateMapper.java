package joshie.harvest.crops;

import com.google.common.collect.Maps;
import joshie.harvest.api.crops.ICrop;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class CropStateMapper extends StateMapperBase {
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
        for (IBlockState iblockstate : blockIn.getBlockState().getValidStates()) {
            mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
        }

        for (ICrop crop: Crop.crops) {
            for (IBlockState state: crop.getStateHandler().getValidStates()) {
                mapStateModelLocations.put(state, getCropResourceLocation(crop, state));
            }
        }

        return mapStateModelLocations;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(state.getProperties());
        return new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
    }

    protected ModelResourceLocation getCropResourceLocation(ICrop crop, IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(state.getProperties());
        return new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()) + "_" + crop.getUnlocalizedName(), this.getPropertyString(map));
    }
}