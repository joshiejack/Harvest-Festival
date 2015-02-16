package joshie.harvestmoon.mining;


public class MineAir extends MineBlock {
    public MineAir() {}
    public MineAir(int dim, int x, int y, int z) {
        super(dim, x, y, z);
    }
    
    @Override
    public String getType() {
        return "air";
    }
}
