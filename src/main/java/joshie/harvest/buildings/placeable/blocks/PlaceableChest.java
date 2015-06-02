package joshie.harvest.buildings.placeable.blocks;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class PlaceableChest extends PlaceableBlock {
    private String chestType;
    
    public PlaceableChest(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }
    
    public PlaceableChest(Block block, int meta, int offsetX, int offsetY, int offsetZ, String chestType) {
        this(block, meta, offsetX, offsetY, offsetZ);
        this.chestType = chestType;
    }
    
    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.TORCHES;
    }


    @Override
    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        if (meta == 3) {
            if (n2) {
                return swap ? 4 : 2;
            } else if (swap) {
                return 5;
            }
        } else if (meta == 5) {
            if (n1) {
                return swap ? 2 : 4;
            } else if (swap) {
                return 3;
            }
        } else if (meta == 4) {
            if (n1) {
                return swap ? 3 : 5;
            } else if (swap) {
                return 2;
            }
        } else if (meta == 2) {
            if (n2) {
                return swap ? 5 : 3;
            } else if (swap) {
                return 4;
            }
        }

        return meta;
    }
    
    @Override
    public boolean place(UUID uuid, World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        if(!super.place(uuid, world, x, y, z, n1, n2, swap)) return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (chestType != null && tile instanceof IInventory) {
            WeightedRandomChestContent.generateChestContents(world.rand, ChestGenHooks.getItems(chestType, world.rand), (IInventory)tile, 10);
            return true;
        }
        
        return false;
    }
}
