package joshie.harvest.mining.data;


import net.minecraft.util.math.BlockPos;

public class MineWall extends MineBlock {
    public MineWall() {
    }

    public MineWall(int dim, BlockPos pos) {
        super(dim, pos);
    }

    @Override
    public String getType() {
        return "wall";
    }
}