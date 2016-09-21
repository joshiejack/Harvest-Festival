package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.core.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class PlaceableChest extends PlaceableBlock {
    private ResourceLocation chestType;

    public PlaceableChest() {}
    public PlaceableChest(String name, IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
        this.chestType = new ResourceLocation(MODID, name);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos);
        if (chestType != null && tile instanceof TileEntityChest) {
            ((TileEntityChest)tile).setLootTable(chestType, world.rand.nextLong());
        }
    }
}