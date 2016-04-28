package joshie.harvest.mining.data;

import joshie.harvest.api.WorldLocation;
import net.minecraft.util.math.BlockPos;

public abstract class MineBlock extends WorldLocation {
    public MineBlock() {
    }

    public MineBlock(int dim, BlockPos pos) {
        super(dim, pos);
    }

    public void newDay(int level) {
        return;
    }

    public String getType() {
        return "";
    }
}