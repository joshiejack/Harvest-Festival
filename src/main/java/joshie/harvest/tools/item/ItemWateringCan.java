package joshie.harvest.tools.item;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.handlers.SingleFluidHandler;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class ItemWateringCan extends ItemTool<ItemWateringCan> {
    private static final double MAX_WATER = 128D;

    public ItemWateringCan() {
        super("watering_can", new HashSet<>());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        SingleFluidHandler handler = new SingleFluidHandler(stack, FluidRegistry.WATER, (int) MAX_WATER);
        //TODO: Remove in 0.7+
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Water")) {
            handler.fill(new FluidStack(FluidRegistry.WATER, stack.getTagCompound().getByte("Water")), true);
        }

        return handler;
    }

    @Override
    public int getFront(ToolTier tier) {
        switch (tier) {
            case BASIC:
            case COPPER:
                return 0;
            case SILVER:
                return 2;
            case GOLD:
                return 2;
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 5;
            case MYTHIC:
                return 11;
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
                return 1;
            case SILVER:
                return 1;
            case GOLD:
                return 2;
            case MYSTRIL:
                return 3;
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
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int water = getCapacity(stack);
        return (MAX_WATER - water) / MAX_WATER;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(attemptToFill(world, player, stack)) return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        else {
            waterCrops(world, player, getMovingObjectPositionFromPlayer(world, player), stack, getTier(stack));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    private EnumActionResult hydrate(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        if (HFApi.crops.hydrateSoil(player, world, pos)) {
            displayParticle(world, pos, EnumParticleTypes.WATER_SPLASH, Blocks.WATER.getDefaultState());
            playSound(world, pos, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.NEUTRAL);
            ToolHelper.performTask(player, stack, this);
            if (!player.capabilities.isCreativeMode) {
                getCapability(stack).drain(1, true);
            }
            return EnumActionResult.SUCCESS;
        } else return EnumActionResult.FAIL;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean attemptToFill(World world, EntityPlayer player, ItemStack stack) {
        RayTraceResult rayTraceResult = this.rayTrace(world, player, true);
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            IBlockState state = world.getBlockState(rayTraceResult.getBlockPos());
            if (state.getMaterial() == Material.WATER) {
                return getCapability(stack).fill(new FluidStack(FluidRegistry.WATER, 128), true) > 0;
            }
        }

        return false;
    }

    private IFluidHandler getCapability(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
    }

    private int getCapacity(ItemStack stack) {
        IFluidTankProperties properties = getCapability(stack).getTankProperties()[0];
        if (properties.getContents() == null) return 0;
        else return properties.getContents().amount;
    }

    private void waterCrops(World world, EntityPlayer player, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null) {
            BlockPos pos = result.getBlockPos();
            EnumFacing front = EntityHelper.getFacingFromEntity(player);
            IBlockState initialState = world.getBlockState(pos);
            Block initial = initialState.getBlock();
            if (CropHelper.getWateringHandler(world, pos, initialState) == null && (!(initial instanceof IPlantable))) {
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
                            } else hydrate(player, stack, world, position);
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        ToolTier tier = getTier(stack);
        int width = 1 + (2 * getSides(tier));
        int depth = 1 + getFront(tier);
        tooltip.add(TextFormatting.AQUA + TextHelper.formatHF("wateringcan.tooltip.dimensions", width, depth));

        if (advanced) {
            tooltip.add("Water: " + getCapacity(stack));
            tooltip.add("Level: " + getLevel(stack));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < ToolTier.values().length; i++) {
            ItemStack unleveled = new ItemStack(item, 1, i);
            getCapability(unleveled).fill(new FluidStack(FluidRegistry.WATER, 128), true);
            list.add(unleveled);
        }
    }
}