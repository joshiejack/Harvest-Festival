package joshie.harvest.mining.data;



public class MineWall extends MineBlock {
    public MineWall() {}
    public MineWall(int dim, int x, int y, int z) {
        super(dim, x, y, z);
    }
    
    @Override
    public String getType() {
        return "wall";
    }
}
