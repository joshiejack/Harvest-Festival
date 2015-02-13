package joshie.harvestmoon.blocks.tiles;

import static joshie.harvestmoon.cooking.Utensil.KNIFE;
import static joshie.harvestmoon.cooking.Utensil.ROLLING_PIN;
import static joshie.harvestmoon.cooking.Utensil.WHISK;
import joshie.harvestmoon.blocks.BlockGeneral;
import joshie.harvestmoon.cooking.Utensil;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class TileKitchen extends TileCooking {
    @Override
    public void updateUtensil() {
        utensil = getUtensil(worldObj, xCoord, yCoord + 1, zCoord);
    }

    @Override
    public short getCookingTime(Utensil utensil) {
        return (short) (utensil == KNIFE ? 25 : utensil == WHISK ? 45 : utensil == ROLLING_PIN ? 35 : 30);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public Utensil getUtensil(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block == HMBlocks.tiles) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta == BlockGeneral.CHOPPING_BOARD) return KNIFE;
            else if (meta == BlockGeneral.MIXING_BOWL) return WHISK;
            else if (meta == BlockGeneral.BAKING_GLASS) return ROLLING_PIN;
        }

        return Utensil.KITCHEN;
    }
}
