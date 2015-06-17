package joshie.harvest.mining.data;

import joshie.harvest.api.WorldLocation;

public abstract class MineBlock extends WorldLocation {
    public MineBlock() {}
    public MineBlock(int dim, int x, int y, int z) {
        super(dim, x, y, z);
    }

    public void newDay(int level) {
        return;
    }

    public String getType() {
        return "";
    }
}
