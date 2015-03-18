package joshie.harvestmoon.core.handlers.events;

import java.util.ArrayList;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.init.HMCrops;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CropEvents {
    @SubscribeEvent
    public void onBlockHarvested(HarvestDropsEvent event) {
        ArrayList<ItemStack> items = event.drops;
        //If we are silktouching the block, we should ignore it's data
        if ((event.block == Blocks.pumpkin || event.block == Blocks.melon_block) && !event.isSilkTouching) {
            ICropData crop = null;
            if (event.blockMetadata == 0) {
                crop = HMApi.CROPS.getCropAtLocation(event.world, event.x - 1, event.y, event.z);
            } else if (event.blockMetadata == 1) {
                crop = HMApi.CROPS.getCropAtLocation(event.world, event.x, event.y, event.z + 1);
            } else if (event.blockMetadata == 2) {
                crop = HMApi.CROPS.getCropAtLocation(event.world, event.x, event.y, event.z - 1);
            } else if (event.blockMetadata == 3) {
                crop = HMApi.CROPS.getCropAtLocation(event.world, event.x + 1, event.y, event.z);
            }
            
            if (crop == null || crop.getCrop() == HMCrops.null_crop) { //If the crop failed to find, grab the pumpkin itself
                crop = HMApi.CROPS.getCropAtLocation(event.world, event.x, event.y, event.z);
                System.out.println("HUH?");
            }
                        
            if (crop != null) {
                items.clear(); //Clear out the old drops for this block
                items.add(crop.getCrop().getCropStackForQuality(crop.getQuality())); //Add in the quality verified version
                
                if (event.blockMetadata == 0) {
                    CropHelper.plantCrop(event.harvester, event.world, event.x - 1, event.y, event.z, crop.getCrop(), crop.getQuality(), 1);
                } else if (event.blockMetadata == 1) {
                    CropHelper.plantCrop(event.harvester, event.world, event.x, event.y, event.z + 1, crop.getCrop(), crop.getQuality(), 1);
                } else if (event.blockMetadata == 2) {
                    CropHelper.plantCrop(event.harvester, event.world, event.x, event.y, event.z - 1, crop.getCrop(), crop.getQuality(), 1);
                } else if (event.blockMetadata == 3) {
                    CropHelper.plantCrop(event.harvester, event.world, event.x + 1, event.y, event.z, crop.getCrop(), crop.getQuality(), 1);
                }
            }
        }
    }
}
