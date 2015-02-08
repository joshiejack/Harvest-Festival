package joshie.harvestmoon.buildings.placeable.blocks;

import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class PlaceableSignWall extends PlaceableBlock {
    private String[] text;

    public PlaceableSignWall(Block block, int meta, int offsetX, int offsetY, int offsetZ, String[] text) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.text = text;
    }
    
    @Override
    protected boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }

    @Override
    protected int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 2) {
            if (n2) {
                meta = 3;
                if (swap) {
                    meta = 5;
                }
            } else if (swap) {
                meta = 4;
            }
        } else if (meta == 3) {
            if (n2) {
                meta = 2;
                if (swap) {
                    meta = 4;
                }
            } else if (swap) {
                meta = 5;
            }
        } else if (meta == 4) {
            if (n1) {
                meta = 5;
                if (swap) {
                    meta = 3;
                }
            } else if (swap) {
                meta = 2;
            }
        } else if (meta == 5) {
            if (n1) {
                meta = 4;
                if (swap) {
                    meta = 2;
                }
            } else if (swap) {
                meta = 3;
            }
        }

        return meta;
    }

    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        super.place(world, x, y, z, n1, n2, swap);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntitySign) {
            ((TileEntitySign) tile).signText = text;
        }
    }
}
