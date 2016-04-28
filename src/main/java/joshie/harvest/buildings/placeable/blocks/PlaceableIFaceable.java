package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncOrientation;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableIFaceable extends PlaceableBlock {
    private EnumFacing facing;
    private String chestType;

    public PlaceableIFaceable(Block block, int meta, int offsetX, int offsetY, int offsetZ, EnumFacing facing) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.facing = facing;
    }

    public PlaceableIFaceable(Block block, int meta, int offsetX, int offsetY, int offsetZ, EnumFacing facing, String chestType) {
        this(block, meta, offsetX, offsetY, offsetZ, facing);
        this.chestType = chestType;
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    private EnumFacing getFacing(boolean n1, boolean n2, boolean swap) {
        if (facing == EnumFacing.NORTH) {
            if (n2) {
                return swap ? EnumFacing.EAST : EnumFacing.SOUTH;
            } else if (swap) {
                return EnumFacing.WEST;
            }
        } else if (facing == EnumFacing.SOUTH) {
            if (n2) {
                return swap ? EnumFacing.WEST : EnumFacing.NORTH;
            } else if (swap) {
                return EnumFacing.EAST;
            }
        } else if (facing == EnumFacing.WEST) {
            if (n1) {
                return swap ? EnumFacing.SOUTH : EnumFacing.EAST;
            } else if (swap) {
                return EnumFacing.NORTH;
            }
        } else if (facing == EnumFacing.EAST) {
            if (n1) {
                return swap ? EnumFacing.NORTH : EnumFacing.WEST;
            } else if (swap) {
                return EnumFacing.SOUTH;
            }
        }

        return facing;
    }

    @Override
    public boolean place(UUID uuid, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap) {
        if (!super.place(uuid, world, pos, state, n1, n2, swap)) return false;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            EnumFacing orientation = getFacing(n1, n2, swap);
            ((IFaceable) tile).setFacing(orientation);
            PacketHandler.sendAround(new PacketSyncOrientation(world.provider.getDimension(), pos, orientation), tile);
            return true;
        }

        /*if (chestType != null && tile instanceof IInventory) { //TODO
            WeightedRandomChestContent.generateChestContents(world.rand, ChestGenHooks.getItems(chestType, world.rand), (IInventory) tile, 10);
            return true;
        }*/

        return false;
    }
}