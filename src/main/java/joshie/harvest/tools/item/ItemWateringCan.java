package joshie.harvest.tools.item;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.base.item.ItemToolChargeable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.tools.ToolHelper;
import joshie.harvest.crops.block.BlockHFCrops;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

//TODO: Switch to Capbility for Water Supply
public class ItemWateringCan extends ItemToolChargeable implements IFluidContainerItem {
    public ItemWateringCan() {
        super("watering_can", new HashSet<>());
    }

    @Override
    public int getFront(ToolTier tier) {
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
    public int getSides(ToolTier tier) {
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
    public boolean canBeDamaged() {
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasTagCompound()) {
            int water = stack.getTagCompound().getByte("Water");
            return (128D - water) / 128D;
        } else return 0D;
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return container.hasTagCompound() ? new FluidStack(FluidRegistry.WATER, container.getTagCompound().getByte("Water")) : null;
    }

    @Override
    public int getCapacity(ItemStack container) {
        return container.hasTagCompound() ? container.getTagCompound().getByte("Water") : 0;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() != FluidRegistry.WATER) {
            return 0;
        } else {
            if (!container.hasTagCompound()) {
                container.setTagCompound(new NBTTagCompound());
            }

            int current_capacity = container.getTagCompound().getByte("Water");
            int max_fill_capacity = (Math.max(0, Byte.MAX_VALUE - current_capacity));
            int amount_filled = 0;
            if (resource.amount >= max_fill_capacity) {
                amount_filled = max_fill_capacity;
            } else amount_filled = resource.amount;

            int new_amount = (current_capacity + amount_filled);

            if (doFill) {
                container.getTagCompound().setByte("Water", (byte) new_amount);
            }

            return amount_filled;
        }
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (maxDrain == 1) {
            if (container.hasTagCompound()) {
                byte water = container.getTagCompound().getByte("Water");
                if (water >= 1) {
                    if (doDrain) {
                        container.getTagCompound().setByte("Water", (byte) (water - 1));
                    }

                    return new FluidStack(FluidRegistry.WATER, 1);
                } else return null;
            }
        }

        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(attemptToFill(world, player, stack))  return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        else return super.onItemRightClick(stack, world, player, hand);
    }

    private EnumActionResult hydrate(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        if (HFApi.crops.hydrateSoil(player, world, pos)) {
            displayParticle(world, pos, EnumParticleTypes.WATER_SPLASH, Blocks.WATER.getDefaultState());
            playSound(world, pos, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.NEUTRAL);
            ToolHelper.performTask(player, stack, getExhaustionRate(stack));
            if (!player.capabilities.isCreativeMode) {
                drain(stack, 1, true);
            }
            return EnumActionResult.SUCCESS;
        } else return EnumActionResult.FAIL;
    }

    private boolean attemptToFill(World world, EntityPlayer player, ItemStack stack) {
        RayTraceResult rayTraceResult = this.rayTrace(world, player, true);
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            IBlockState state = world.getBlockState(rayTraceResult.getBlockPos());
            if (state.getMaterial() == Material.WATER) {
                return fill(stack, new FluidStack(FluidRegistry.WATER, 128), true) > 0;
            }
        }

        return false;
    }

    @Override
    protected void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            BlockPos pos = result.getBlockPos();
            EnumFacing front = EntityHelper.getFacingFromEntity(entity);
            Block initial = world.getBlockState(pos).getBlock();
            if ((initial != Blocks.FARMLAND) && (!(initial instanceof IPlantable))) {
                return;
            }

            //Facing North, We Want East and West to be 1, left * this.left
            for (int y2 = pos.getY() - 1; y2 <= pos.getY(); y2++) {
                for (int x2 = getXMinus(tier, front, pos.getX()); x2 <= getXPlus(tier, front, pos.getX()); x2++) {
                    for (int z2 = getZMinus(tier, front, pos.getZ()); z2 <= getZPlus(tier, front, pos.getZ()); z2++) {
                        if (getCapacity(stack) > 0 && canUse(stack)) {
                            BlockPos position = new BlockPos(x2, y2, z2);
                            IBlockState state = world.getBlockState(position);
                            PlantSection section = BlockHFCrops.getSection(state);
                            if (section != null) {
                                int down = section == PlantSection.BOTTOM ? 1 : 2;
                                hydrate(player, stack, world, position.down(down));
                            } else if (state.getBlock() == Blocks.FARMLAND) {
                                hydrate(player, stack, world, position);
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (advanced) {
            tooltip.add("Water: " + getCapacity(stack));
            tooltip.add("Level: " + getLevel(stack));
        }
    }
}