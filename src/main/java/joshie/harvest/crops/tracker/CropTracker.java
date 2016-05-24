package joshie.harvest.crops.tracker;

import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.HFTracker;
import joshie.harvest.crops.CropData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class CropTracker extends HFTracker {
    HashMap<BlockPos, ICropData> cropTracker = new HashMap<>();

    public ICropData getCropDataForLocation(BlockPos pos) {
        ICropData data = cropTracker.get(pos);
        return data != null ? data : new CropData(pos);
    }

    public boolean canBonemeal(BlockPos pos) {
        ICropData data = getCropDataForLocation(pos);
        return data.getStage() < data.getCrop().getStages();
    }

    public boolean plantCrop(EntityPlayer player, BlockPos pos, ICrop crop, int stage) {
        return true;
    }

    public ItemStack harvest(EntityPlayer player, BlockPos pos) {
        ICropData data = getCropDataForLocation(pos);
        ItemStack harvest = data.harvest(player, true);
        if (harvest != null) {
            if (data.getCrop().getRegrowStage() < 0) {
                removeCrop(pos);
            }

            return harvest;
        } else return null;
    }

    public void removeCrop(BlockPos pos) {
        cropTracker.remove(pos);
    }

    public void hydrate(BlockPos pos, IBlockState state) {}
    public void setWithered(BlockPos pos, ICropData data) {}
    public void grow(BlockPos pos) {}
    public void newDay() {}
    public void sendUpdateToClient(EntityPlayerMP player, BlockPos pos) {}
    public void updateClient(BlockPos position, ICropData data, boolean isRemoval) {}
    public void doRain() {}
}