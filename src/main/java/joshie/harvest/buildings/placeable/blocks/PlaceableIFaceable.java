package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncOrientation;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableIFaceable extends PlaceableBlock {
    private EnumFacing facing;
    private ResourceLocation chestType;

    public PlaceableIFaceable(Block block, int meta, int offsetX, int offsetY, int offsetZ, EnumFacing facing) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.facing = facing;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Direction direction) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            EnumFacing orientation = direction.getRotation().rotate(direction.getMirror().mirror(this.facing));
            ((IFaceable) tile).setFacing(orientation);
            PacketHandler.sendAround(new PacketSyncOrientation(world.provider.getDimension(), pos, orientation), tile);
        }
    }
}