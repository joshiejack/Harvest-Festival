package joshie.harvest.crops;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class CropTracker {
    protected HashMap<WorldLocation, ICropData> crops = new HashMap<WorldLocation, ICropData>();

    protected WorldLocation getCropKey(World world, BlockPos pos) {
        return new WorldLocation(world.provider.getDimension(), pos);
    }

    protected WorldLocation getFarmlandKey(World world, BlockPos pos) {
        return new WorldLocation(world.provider.getDimension(), pos.up());
    }

    public ICropData getCropDataForLocation(World world, BlockPos pos) {
        WorldLocation location = getCropKey(world, pos);
        ICropData data = crops.get(location);
        return data != null ? data : new CropData(location);
    }

    public boolean canBonemeal(World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        return data.getStage() < data.getCrop().getStages();
    }

    public boolean plantCrop(EntityPlayer player, World world, BlockPos pos, ICrop crop, int stage) {
        return true;
    }

    public ItemStack getHarvest(EntityPlayer player, World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        return data.harvest(player, false);
    }

    public ItemStack harvest(EntityPlayer player, World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        ItemStack harvest = data.harvest(player, true);
        if (harvest != null) {
            if (data.getCrop().getRegrowStage() < 0) {
                removeCrop(world, pos);
            }

            return harvest;
        } else return null;
    }

    public void removeCrop(World world, BlockPos pos) {
        ICropData data = getCropDataForLocation(world, pos);
        crops.remove(data.getLocation());
    }

    public void hydrate(World world, BlockPos pos, IBlockState state) {
    }

    public void setWithered(ICropData data) {
    }

    public void grow(World world, BlockPos pos) {
    }

    public void newDay() {
    }

    public void sendUpdateToClient(EntityPlayerMP player, World world, BlockPos pos) {
    }

    public void updateClient(boolean isRemoval, WorldLocation location, ICropData data) {
    }

    public void doRain() {
    }
}