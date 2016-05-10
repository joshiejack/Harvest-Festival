package joshie.harvest.crops;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
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
    protected TIntObjectMap<HashMap<BlockPos, ICropData>> dimensions = new TIntObjectHashMap<>();

    protected HashMap<BlockPos, ICropData> getDimensionData(World world) {
        return getDimensionData(world.provider.getDimension());
    }

    protected HashMap<BlockPos, ICropData> getDimensionData(int dimension) {
        HashMap<BlockPos, ICropData> map = dimensions.get(dimension);
        if (map == null) {
            map = new HashMap<>();
            dimensions.put(dimension, map);
        }

        return map;
    }

    public ICropData getCropDataForLocation(World world, BlockPos pos) {
        ICropData data = getDimensionData(world.provider.getDimension()).get(pos);
        return data != null ? data : new CropData(pos, world.provider.getDimension());
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
        getDimensionData(world.provider.getDimension()).remove(pos);
    }

    public void hydrate(World world, BlockPos pos, IBlockState state) {}
    public void setWithered(World world, BlockPos pos, ICropData data) {}
    public void grow(World world, BlockPos pos) {}
    public void newDay(World world) {}
    public void sendUpdateToClient(EntityPlayerMP player, World world, BlockPos pos) {}
    public void updateClient(int dimension, BlockPos position, ICropData data, boolean isRemoval) {}
    public void doRain(World world) {}
}