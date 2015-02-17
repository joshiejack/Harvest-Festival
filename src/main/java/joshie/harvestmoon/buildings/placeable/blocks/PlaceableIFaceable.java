package joshie.harvestmoon.buildings.placeable.blocks;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSyncOrientation;
import joshie.harvestmoon.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PlaceableIFaceable extends PlaceableBlock {
    private ForgeDirection dir;

    public PlaceableIFaceable(Block block, int meta, int offsetX, int offsetY, int offsetZ, ForgeDirection dir) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.dir = dir;
    }

    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    private ForgeDirection getFacing(boolean n1, boolean n2, boolean swap) {
        if (dir == NORTH) {
            if (n2) {
                dir = SOUTH;
                if (swap) {
                    dir = EAST;
                }
            } else if (swap) {
                dir = WEST;
            }
        } else if (dir == SOUTH) {
            if (n2) {
                dir = NORTH;
                if (swap) {
                    dir = WEST;
                }
            } else if (swap) {
                dir = EAST;
            }
        } else if (dir == WEST) {
            if (n1) {
                dir = EAST;
                if (swap) {
                    dir = SOUTH;
                }
            } else if (swap) {
                dir = NORTH;
            }
        } else if (dir == EAST) {
            if (n1) {
                dir = WEST;
                if (swap) {
                    dir = NORTH;
                }
            } else if (swap) {
                dir = SOUTH;
            }
        }

        return dir;
    }

    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        super.place(world, x, y, z, n1, n2, swap);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFaceable) {
            ForgeDirection orientation = getFacing(n1, n2, swap);
            ((IFaceable) tile).setFacing(orientation);
            PacketHandler.sendAround(new PacketSyncOrientation(world.provider.dimensionId, x, y, z, orientation), tile);
        }
    }
}
