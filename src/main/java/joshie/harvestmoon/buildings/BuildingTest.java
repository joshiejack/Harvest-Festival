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
        blocks = new Block[8];
        metas = new int[8];
        offsetX = new int[8];
        offsetY = new int[8];
        offsetZ = new int[8];
        blocks[0] = Blocks.grass;
        metas[0] = 0;
        offsetX[0] = 0;
        offsetY[0] = 0;
        offsetZ[0] = 0;
        blocks[1] = Blocks.grass;
        metas[1] = 0;
        offsetX[1] = 0;
        offsetY[1] = 0;
        offsetZ[1] = 1;
        blocks[2] = Blocks.grass;
        metas[2] = 0;
        offsetX[2] = 0;
        offsetY[2] = 0;
        offsetZ[2] = 2;
        blocks[3] = Blocks.grass;
        metas[3] = 0;
        offsetX[3] = 1;
        offsetY[3] = 0;
        offsetZ[3] = 0;
        blocks[4] = Blocks.bed;
        metas[4] = 10;
        offsetX[4] = 1;
        offsetY[4] = 1;
        offsetZ[4] = 0;
        blocks[5] = Blocks.grass;
        metas[5] = 0;
        offsetX[5] = 1;
        offsetY[5] = 0;
        offsetZ[5] = 1;
        blocks[6] = Blocks.bed;
        metas[6] = 2;
        offsetX[6] = 1;
        offsetY[6] = 1;
        offsetZ[6] = 1;
        blocks[7] = Blocks.grass;
        metas[7] = 0;
        offsetX[7] = 1;
        offsetY[7] = 0;
        offsetZ[7] = 2;


    }
}
