package uk.joshiejack.penguinlib.item.base;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.events.UseWateringCanEvent;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.util.handlers.SingleFluidHandler;
import uk.joshiejack.penguinlib.util.helpers.forge.FluidHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EffectHelper;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import java.util.Set;

public class ItemBaseWateringCan extends ItemSingular {
    private final int maxWater;

    public ItemBaseWateringCan(ResourceLocation registry, int maxWater) {
        super(registry);
        this.maxWater = maxWater;
        setHasSubtypes(true);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new SingleFluidHandler(stack, FluidRegistry.WATER, maxWater);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (double) (maxWater - FluidHelper.getFluidCapacityFromStack(stack)) / (double) maxWater;
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        return FluidHelper.getFluidCapacityFromStack(stack) > 0 ? 0x006DD9 : 0x555555;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(attemptToFill(world, player, stack)) return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        else return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean attemptToFill(World world, EntityPlayer player, ItemStack stack) {
        RayTraceResult rayTraceResult = this.rayTrace(world, player, true);
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            IBlockState state = world.getBlockState(rayTraceResult.getBlockPos());
            if (state.getMaterial() == Material.WATER) {
                return FluidHelper.fillContainer(stack, maxWater);
            }
        }

        return false;
    }

    protected void applyBonemealEffect(World world, BlockPos pos, EntityPlayer player, ItemStack itemstack, EnumHand hand) {}

    private Event.Result onWateringCanUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos) {
        UseWateringCanEvent event = new UseWateringCanEvent(player, stack, worldIn, pos);
        if (MinecraftForge.EVENT_BUS.post(event)) return Event.Result.DENY;
        if (event.getResult() == Event.Result.ALLOW) {
            if (!player.capabilities.isCreativeMode) {
                FluidHelper.drainContainer(stack, 1);
            }

            return Event.Result.ALLOW;
        }

        return Event.Result.DEFAULT;
    }

    public Set<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        return Sets.newHashSet(pos, pos.down());
    }

    public boolean water(EntityPlayer player, World world, BlockPos pos, ItemStack stack, EnumHand hand) {
        applyBonemealEffect(world, pos, player, stack, hand); //Do this stuff
        Event.Result hook = onWateringCanUse(stack, player, world, pos);
        if (hook != Event.Result.DEFAULT) return hook == Event.Result.ALLOW;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockFarmland && state != BlockStates.WET_SOIL) {
            world.setBlockState(pos, BlockStates.WET_SOIL);
            if (!player.capabilities.isCreativeMode && !stack.isEmpty()) {
                FluidHelper.drainContainer(stack, 1);
            }

            return true;
        } else return false;
    }

    private void onWatered(EntityPlayer player, World world, BlockPos pos, ItemStack itemstack) {
        MinecraftForge.EVENT_BUS.post(new UseWateringCanEvent.Post(player, itemstack, world, pos));
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (FluidHelper.getFluidCapacityFromStack(itemstack) > 0) {
            if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                boolean used = false;
                for (BlockPos target: getPositions(player, world, pos)) {
                    if (FluidHelper.getFluidCapacityFromStack(itemstack) <= 0) break;
                    if (water(player, world, target, itemstack, hand)) {
                        EffectHelper.playSound(world, target, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.NEUTRAL);
                        EffectHelper.displayParticle(world, target, EnumParticleTypes.WATER_SPLASH, Blocks.WATER.getDefaultState());
                        onWatered(player, world, target, itemstack);
                        used = true;
                    }
                }

                return used ? EnumActionResult.SUCCESS: EnumActionResult.PASS;
            }
        } else return EnumActionResult.FAIL;
    }

    private ItemStack createFilledWateringCan(ItemStack stack) {
        FluidHelper.fillContainer(stack, maxWater);
        return stack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(createFilledWateringCan(new ItemStack(this)));
        }
    }
}
