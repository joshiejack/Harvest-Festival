package joshie.harvestmoon.buildings.placeable.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;


public class PlaceableFlowerPot extends PlaceableBlock {
    public boolean canCactus;
    
    public PlaceableFlowerPot(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }
    
    public PlaceableFlowerPot(Block block, int meta, int offsetX, int offsetY, int offsetZ, boolean canCactus) {
        super(block, meta, offsetX, offsetY, offsetZ);
        this.canCactus = canCactus;
    }
    
    @Override
    public void place(World world, int x, int y, int z, boolean n1, boolean n2, boolean swap) {
        super.place(world, x, y, z, n1, n2, swap);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityFlowerPot) {
            TileEntityFlowerPot pot = (TileEntityFlowerPot) tile;
            ItemStack random = getRandomPlant(world.rand);
            pot.func_145964_a(random.getItem(), random.getItemDamage());
        }
    }
    
    private ItemStack getRandomPlant(Random rand) {
        Block block = null;
        int meta = 0;
        int what = rand.nextInt(13) + 1;
        switch(what) {
        case 1:
            block = Blocks.red_flower;
            meta = 0;
            break;
        case 2:
            block = Blocks.yellow_flower;
            break;
        case 3:
            block = Blocks.sapling;
            meta = 0;
            break;
        case 4:
            block = Blocks.sapling;
            meta = 1;
            break;
        case 5:
            block = Blocks.sapling;
            meta = 2;
            break;
        case 6:
            block = Blocks.sapling;
            meta = 3;
            break;
        case 7:
            block = Blocks.red_mushroom;
            break;
        case 8:
            block = Blocks.brown_mushroom;
            break;
        case 9:
            block = Blocks.cactus;
            break;
        case 10:
            block = Blocks.deadbush;
            break;
        case 11:
            block = Blocks.tallgrass;
            meta = 2;
            break;
        case 12:
            block = Blocks.sapling;
            meta = 4;
            break;
        case 13:
            block = Blocks.sapling;
            meta = 5;
        }
        
        return new ItemStack(Item.getItemFromBlock(block), 1, meta);
    }
}
