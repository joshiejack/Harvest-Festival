package joshie.harvestmoon.blocks.tiles;

import java.util.ArrayList;

import joshie.harvestmoon.api.cooking.ICookingComponent;
import joshie.harvestmoon.api.cooking.IUtensil;
import joshie.harvestmoon.blocks.BlockCookware;
import joshie.harvestmoon.cooking.Utensil;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

public class TileFryingPan extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.FRYING_PAN;
    }

    @Override
    public boolean hasPrerequisites() {
        Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
        int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
        if (block == HMBlocks.cookware && meta == BlockCookware.OVEN) {
            return true;
        } else return false;
    }
}
