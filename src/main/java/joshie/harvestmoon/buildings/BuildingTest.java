package joshie.harvestmoon.buildings;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BuildingTest extends Building {
    public BuildingTest() {
        reset();
    }
    
    @Override
    public boolean generate(World world, int xCoord, int yCoord, int zCoord) {
        if (!world.isRemote) {
            reset();
        }
        
        return super.generate(world, xCoord, yCoord, zCoord);
    }
    
    public void reset() {
        blocks = new Block[13];
        metas = new int[13];
        offsetX = new int[13];
        offsetY = new int[13];
        offsetZ = new int[13];
        blocks[0] = Blocks.grass;
        metas[0] = 0;
        offsetX[0] = 0;
        offsetY[0] = 0;
        offsetZ[0] = 0;
        blocks[1] = Blocks.coal_ore;
        metas[1] = 0;
        offsetX[1] = 0;
        offsetY[1] = 1;
        offsetZ[1] = 0;
        blocks[2] = Blocks.quartz_stairs;
        metas[2] = 7;
        offsetX[2] = 0;
        offsetY[2] = 2;
        offsetZ[2] = 0;
        blocks[3] = Blocks.grass;
        metas[3] = 0;
        offsetX[3] = 0;
        offsetY[3] = 0;
        offsetZ[3] = 1;
        blocks[4] = Blocks.grass;
        metas[4] = 0;
        offsetX[4] = 1;
        offsetY[4] = 0;
        offsetZ[4] = 0;
        blocks[5] = Blocks.coal_ore;
        metas[5] = 0;
        offsetX[5] = 1;
        offsetY[5] = 1;
        offsetZ[5] = 0;
        blocks[6] = Blocks.quartz_stairs;
        metas[6] = 7;
        offsetX[6] = 1;
        offsetY[6] = 2;
        offsetZ[6] = 0;
        blocks[7] = Blocks.grass;
        metas[7] = 0;
        offsetX[7] = 1;
        offsetY[7] = 0;
        offsetZ[7] = 1;
        blocks[8] = Blocks.grass;
        metas[8] = 0;
        offsetX[8] = 2;
        offsetY[8] = 0;
        offsetZ[8] = 0;
        blocks[9] = Blocks.coal_ore;
        metas[9] = 0;
        offsetX[9] = 2;
        offsetY[9] = 1;
        offsetZ[9] = 0;
        blocks[10] = Blocks.coal_ore;
        metas[10] = 0;
        offsetX[10] = 2;
        offsetY[10] = 2;
        offsetZ[10] = 0;
        blocks[11] = Blocks.grass;
        metas[11] = 0;
        offsetX[11] = 2;
        offsetY[11] = 0;
        offsetZ[11] = 1;
        blocks[12] = Blocks.quartz_stairs;
        metas[12] = 7;
        offsetX[12] = 2;
        offsetY[12] = 2;
        offsetZ[12] = 1;

    }
}
