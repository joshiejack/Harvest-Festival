package joshie.harvest.buildings.placeable.blocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableSignWall extends PlaceableBlock {
    private String[] text;

    public PlaceableSignWall(Block block, int meta, int offsetX, int offsetY, int offsetZ, String[] text) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.text = text;
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 2) {
            if (n2) {
                return swap ? 5 : 3;
            } else if (swap) {
                return 4;
            }
        } else if (meta == 3) {
            if (n2) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 3 : 5;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 5) {
            if (n1) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 3;
            }
        }

        return meta;
    }

    @Override
    public boolean place(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        if (!super.place(uuid, world, x, y, z, n1, n2, swap)) return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntitySign) {
            ((TileEntitySign) tile).signText = text;
            return true;
        } else return false;
    }
}
