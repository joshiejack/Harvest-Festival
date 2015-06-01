package joshie.harvest.crops;

import joshie.harvest.api.crops.ISoilHandler;
import joshie.harvest.init.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class SoilHandlerDefault implements ISoilHandler, IPlantable {
    private EnumPlantType plantable;
    private Block block;

    public SoilHandlerDefault(final EnumPlantType type, Block block) {
        this(type);
        this.block = block;
    }

    public SoilHandlerDefault(final EnumPlantType type) {
        this.plantable = type;
    }

    @Override
    public boolean canSustainPlant(IBlockAccess access, int x, int y, int z, IPlantable plantable) {
        Block below = access.getBlock(x, y - 1, z);
        if (block != null) {
            return below == block && below.canSustainPlant(access, x, y - 1, z, ForgeDirection.UP, this);
        } else return below.canSustainPlant(access, x, y - 1, z, ForgeDirection.UP, this);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return plantable;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return HFBlocks.crops;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }
}
