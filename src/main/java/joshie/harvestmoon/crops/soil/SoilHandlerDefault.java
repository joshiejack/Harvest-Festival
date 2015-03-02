package joshie.harvestmoon.crops.soil;

import joshie.harvestmoon.api.crops.ISoilHandler;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class SoilHandlerDefault implements ISoilHandler, IPlantable {
    private EnumPlantType plantable;

    public SoilHandlerDefault(final EnumPlantType type) {
        this.plantable = type;
    }

    @Override
    public boolean canSustainPlant(IBlockAccess access, int x, int y, int z, IPlantable plantable) {
        return access.getBlock(x, y - 1, z).canSustainPlant(access, x, y, z, ForgeDirection.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return plantable;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return HMBlocks.crops;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }
}
