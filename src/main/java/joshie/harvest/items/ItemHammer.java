package joshie.harvest.items;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.init.HFBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemHammer extends ItemBaseTool {
    @Override
    public int getFront(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 0;
            case COPPER:
                return 1;
            case SILVER:
                return 2;
            case GOLD:
                return 3;
            case MYSTRIL:
                return 5;
            case CURSED:
            case BLESSED:
                return 11;
            case MYTHIC:
                return 17;
            default:
                return 0;
        }
    }

    @Override
    public int getSides(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
            case COPPER:
            case SILVER:
            case GOLD:
            case MYSTRIL:
                return 0;
            case CURSED:
            case BLESSED:
                return 1;
            case MYTHIC:
                return 2;
            default:
                return 0;
        }
    }

// ### Not sure if you need this for later ###	
//    private void doParticles(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
//        displayParticle(world, x, y, z, "blockcrack_3_0");
//        playSound(world, x, y, z, Blocks.farmland.stepSound.getStepResourcePath());
//        PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
//    }
}
