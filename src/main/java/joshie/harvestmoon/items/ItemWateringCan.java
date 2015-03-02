package joshie.harvestmoon.items;

import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.helpers.PlayerHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketWateringCan;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemWateringCan extends ItemBaseTool implements IFluidContainerItem {
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
                return 6;
            case MYTHIC:
                return 12;
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
                return 6;
            case MYTHIC:
                return 10;
            default:
                return 0;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasTagCompound()) {
            byte water = stack.stackTagCompound.getByte("Water");
            return (water / 128) * 100D;
        } else return 0D;
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return container.hasTagCompound() ? new FluidStack(FluidRegistry.WATER, container.stackTagCompound.getByte("Water")) : null;
    }

    @Override
    public int getCapacity(ItemStack container) {
        return container.hasTagCompound() ? container.stackTagCompound.getByte("Water") : 0;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() != FluidRegistry.WATER) {
            return 0;
        } else {
            if (!container.hasTagCompound()) {
                container.setTagCompound(new NBTTagCompound());
            }

            int current_capacity = container.stackTagCompound.getByte("Water");
            int max_fill_capacity = (Math.max(0, Byte.MAX_VALUE - current_capacity));
            int amount_filled = 0;
            if (resource.amount >= max_fill_capacity) {
                amount_filled = max_fill_capacity;
            } else amount_filled = resource.amount;

            int new_amount = (current_capacity + amount_filled);

            if (doFill) {
                container.stackTagCompound.setByte("Water", (byte) new_amount);
            }

            return amount_filled;
        }
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (maxDrain == 1) {
            if (container.hasTagCompound()) {
                byte water = container.stackTagCompound.getByte("Water");
                if (water >= 1) {
                    if (doDrain) {
                        container.stackTagCompound.setByte("Water", (byte) (water - 1));
                    }

                    return new FluidStack(FluidRegistry.WATER, 1);
                } else return null;
            }
        }

        return null;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        attemptToFill(world, player, stack);
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) return false;
        else {
            onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
            PacketHandler.sendToServer(new PacketWateringCan(stack, world, x, y, z));
            return true;
        }
    }

    private boolean hydrate(EntityPlayer player, ItemStack stack, World world, int x, int y, int z) {
        if (CropHelper.hydrate(world, x, y, z)) {
            displayParticle(world, x, y, z, "splash");
            playSound(world, x, y, z, "game.neutral.swim");
            PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
            if (!player.capabilities.isCreativeMode) {
                drain(stack, 1, true);
            }

            return true;
        } else return false;
    }

    private boolean attemptToFill(World world, EntityPlayer player, ItemStack stack) {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int posX = movingobjectposition.blockX;
                int posY = movingobjectposition.blockY;
                int posZ = movingobjectposition.blockZ;
                Block block = world.getBlock(posX, posY, posZ);
                if (block.getMaterial() == Material.water) {
                    return fill(stack, new FluidStack(FluidRegistry.WATER, 128), true) > 0;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (attemptToFill(world, player, stack)) {
            return true;
        } else {
            ForgeDirection front = joshie.harvestmoon.core.helpers.generic.DirectionHelper.getFacingFromEntity(player);
            Block initial = world.getBlock(x, y, z);
            if (!(initial instanceof BlockFarmland) && (!(initial instanceof IPlantable))) {
                return false;
            }

            boolean watered = false;
            //Facing North, We Want East and West to be 1, left * this.left
            for (int y2 = y - 1; y2 <= y; y2++) {
                for (int x2 = getXMinus(stack, front, x); x2 <= getXPlus(stack, front, x); x2++) {
                    for (int z2 = getZMinus(stack, front, z); z2 <= getZPlus(stack, front, z); z2++) {
                        if (getCapacity(stack) > 0) {
                            Block block = world.getBlock(x2, y2, z2);
                            if (block instanceof IPlantable) {
                                watered = hydrate(player, stack, world, x2, y2 - 1, z2);
                            } else if (block instanceof BlockFarmland) {
                                watered = hydrate(player, stack, world, x2, y2, z2);
                            }
                        }
                    }
                }
            }

            return watered;
        }
    }
}
