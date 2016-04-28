package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.cooking.Utensil;
import net.minecraft.block.state.IBlockState;

public class TileMixer extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.MIXER;
    }

    @Override
    public boolean hasPrerequisites() {
        IBlockState state = worldObj.getBlockState(pos.down());
        int meta = state.getBlock().getMetaFromState(state);
        BlockCookware.Cookware cookware = getEnumFromState(state);
        if (state.getBlock() == HFBlocks.COOKWARE && meta == BlockCookware.Cookware.COUNTER) {
            return true;
        } else return false;
    }
}