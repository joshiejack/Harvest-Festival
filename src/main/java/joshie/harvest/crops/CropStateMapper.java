package joshie.harvest.crops;

import com.google.common.collect.Maps;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.crops.blocks.BlockHFFarmland.Moisture;
import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;

import java.util.Map;

public class CropStateMapper extends StateMapperBase {
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
        if (blockIn instanceof BlockHFCrops) {
            for (IBlockState iblockstate : blockIn.getBlockState().getValidStates()) {
                mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
            }

            for (ICrop crop : HFApi.crops.getCrops()) {
                if (crop.getStateHandler().getValidStates() == null) continue;
                for (IBlockState state : crop.getStateHandler().getValidStates()) {
                    mapStateModelLocations.put(state, getCropResourceLocation(crop, state));
                }
            }
        } else {
            mapStateModelLocations.put(HFBlocks.FARMLAND.getStateFromEnum(Moisture.DRY), getModelResourceLocation(Blocks.FARMLAND.getStateFromMeta(0)));
            mapStateModelLocations.put(HFBlocks.FARMLAND.getStateFromEnum(Moisture.WET), getModelResourceLocation(Blocks.FARMLAND.getStateFromMeta(7)));
        }

        return mapStateModelLocations;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(state.getProperties());
        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
    }

    protected ModelResourceLocation getCropResourceLocation(ICrop crop, IBlockState state) {
        Map <IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(state.getProperties());
        return new ModelResourceLocation(crop.getResource().getResourceDomain() + ":crops_block_" + crop.getResource().getResourcePath(), this.getPropertyString(map));
    }
}