package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.cooking.Utensil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class TilePot extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.POT;
    }

    @Override
    public boolean hasPrerequisites() {
        IBlockState state = worldObj.getBlockState(pos.down());
        int meta = state.getBlock().getMetaFromState(state);
        if (state.getBlock() == HFBlocks.COOKWARE && meta == BlockCookware.Cookware.OVEN) {
            return true;
        } else return false;
    }
}