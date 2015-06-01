package joshie.harvest.items;

import static net.minecraft.block.Block.soundTypeGrass;
import joshie.harvest.api.crops.IBreakCrops;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.core.helpers.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemSickle extends ItemBaseTool implements IBreakCrops {
    @Override
    public int getFront(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
            case COPPER:
                return 0;
            case SILVER:
                return 1;
            case GOLD:
                return 2;
            case MYSTRIL:
                return 4;
            case CURSED:
            case BLESSED:
                return 8;
            case MYTHIC:
                return 14;
            default:
                return 0;
        }
    }

    @Override
    public int getSides(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 0;
            case COPPER:
            case SILVER:
            case GOLD:
                return 1;
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 4;
            case MYTHIC:
                return 7;
            default:
                return 0;
        }
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return (block != Blocks.grass && block.getMaterial() == Material.grass) || block.getMaterial() == Material.leaves || block.getMaterial() == Material.vine ? 10F : func_150893_a(stack, block);
    }

    @Override
    public float getStrengthVSCrops(EntityPlayer player, World world, int x, int y, int z, ItemStack stack) {
        if (!player.canPlayerEdit(x, y, z, 0, stack)) return 0F;
        else {
            ForgeDirection front = joshie.harvest.core.helpers.generic.DirectionHelper.getFacingFromEntity(player);
            Block initial = world.getBlock(x, y, z);
            if (!(initial instanceof BlockCrop)) {
                return 0F;
            }

            //Facing North, We Want East and West to be 1, left * this.left
            boolean changed = false;
            for (int x2 = getXMinus(stack, front, x); x2 <= getXPlus(stack, front, x); x2++) {
                for (int z2 = getZMinus(stack, front, z); z2 <= getZPlus(stack, front, z); z2++) {
                    Block block = world.getBlock(x2, y, z2);
                    if (block instanceof BlockCrop) {
                        if (!world.isRemote) {
                            block.removedByPlayer(world, player, x2, y, z2, true);
                        }

                        displayParticle(world, x2, y, z2, "blockcrack_31_1");
                        playSound(world, x2, y, z2, soundTypeGrass.soundName);
                        PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
                    }
                }
            }
        }

        return 1F;
    }
}
