package joshie.harvest.mining.data;


import net.minecraft.util.math.BlockPos;

public class MineAir extends MineBlock {
    public MineAir() {
    }

    public MineAir(int dim, BlockPos pos) {
        super(dim, pos);
    }

    @Override
    public String getType() {
        return "air";
    }
}