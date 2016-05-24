package joshie.harvest.crops.handlers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.crops.blocks.BlockHFCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.BOTTOM;

public class StateHandlerGrass extends StateHandlerDefault {
    public final TIntObjectMap<AxisAlignedBB> CROP_AABB = new TIntObjectHashMap<AxisAlignedBB>();

    public StateHandlerGrass() {
        super(17);

        for (int i = 0; i < 11; i++) {
            if (i >= 6) {
                CROP_AABB.put(i, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
                CROP_AABB.put(i + 6, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3D + ((i - 6) * 0.1D), 1.0D));
            } else {
                CROP_AABB.put(i, new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4D + (i * 0.1D), 1.0D));
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(PlantSection section, int stage, boolean withered) {
        AxisAlignedBB ret = null;
        if (section == BOTTOM) {
            ret = CROP_AABB.get(stage);
        } else {
            ret = CROP_AABB.get((stage + 6));
        }

        return ret == null ? BlockHFCrops.CROP_AABB : ret;
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (section == BOTTOM) {
            return getState(stage);
        } else {
            return getState(stage + 6);
        }
    }
}