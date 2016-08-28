package joshie.harvest.crops.handlers.state;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class StateHandlerNetherWart extends StateHandlerVanilla {
    public StateHandlerNetherWart() {
        super(Blocks.NETHER_WART);
    }

    @Override
    public IBlockState getState(PlantSection section, int stage, boolean withered) {
        if (stage == 1) return block.getStateFromMeta(0);
        if (stage == 2) return block.getStateFromMeta(1);
        if (stage == 3) return block.getStateFromMeta(2);
        return block.getStateFromMeta(3);
    }
}