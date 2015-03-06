package joshie.harvestmoon.items;

import joshie.harvestmoon.core.helpers.PlayerHelper;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemHoe extends ItemBaseTool {
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

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(x, y, z, side, stack)) return false;
        else {
            ForgeDirection front = joshie.harvestmoon.core.helpers.generic.DirectionHelper.getFacingFromEntity(player);
            Block initial = world.getBlock(x, y, z);
            if (!(world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (initial == Blocks.grass || initial == Blocks.dirt || initial == HMBlocks.dirt))) {
                return false;
            }

            //Facing North, We Want East and West to be 1, left * this.left
            boolean changed = false;
            for (int x2 = getXMinus(stack, front, x); x2 <= getXPlus(stack, front, x); x2++) {
                for (int z2 = getZMinus(stack, front, z); z2 <= getZPlus(stack, front, z); z2++) {
                    Block block = world.getBlock(x2, y, z2);
                    if (world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z)) {
                        changed = true;
                        if ((block == Blocks.grass || block == Blocks.dirt)) {
                            doParticles(stack, player, world, x2, y, z2);
                            if (!world.isRemote) {
                                world.setBlock(x2, y, z2, Blocks.farmland);
                            }
                        }
                    }
                }
            }

            return changed;
        }
    }

    private void doParticles(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
        displayParticle(world, x, y, z, "blockcrack_3_0");
        playSound(world, x, y, z, Blocks.farmland.stepSound.getStepResourcePath());
        PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
    }
}
