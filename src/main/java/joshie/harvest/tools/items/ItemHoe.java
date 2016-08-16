package joshie.harvest.tools.items;

import joshie.harvest.core.helpers.ToolHelper;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import joshie.harvest.core.base.ItemTool;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import javax.annotation.Nullable;
import java.util.HashSet;

public class ItemHoe extends ItemTool {
    public ItemHoe() {
        super("hoe", new HashSet<>());
    }

    @Override
    public int getFront(ToolTier tier) {
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
    public int getSides(ToolTier tier) {
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

    private boolean canHoe(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        if (HFCrops.DISABLE_VANILLA_HOE) return true;
        UseHoeEvent event = new UseHoeEvent(player, stack.copy(), world, pos);
        event.setResult(Result.ALLOW); //Default to allow?
        if (MinecraftForge.EVENT_BUS.post(event)) return false;
        if (event.getResult() != Result.DENY) {
            return true;
        }

        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (count <= 31995 && count % 16 == 0) {
            if (canCharge(stack)) {
                increaseCharge(stack, 1);
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        int charge = (Math.min(7, Math.max(0, getCharge(stack))));
        setCharge(stack, 0); //Reset the charge
        if (!world.isRemote) {
            onFinishedCharging(world, entity, getMovingObjectPositionFromPlayer(world, entity), stack, ToolTier.values()[charge]);
        }
    }

    @Override
    protected void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            BlockPos pos = result.getBlockPos();
            EnumFacing front = DirectionHelper.getFacingFromEntity(player);
            if (!player.canPlayerEdit(pos.offset(front), front, stack)) return;
            else {
                Block initial = world.getBlockState(pos).getBlock();
                if (!(world.isAirBlock(pos.up()) && (initial == Blocks.GRASS || initial == Blocks.DIRT))) {
                    return;
                }

                for (int x2 = getXMinus(tier, front, pos.getX()); x2 <= getXPlus(tier, front, pos.getX()); x2++) {
                    for (int z2 = getZMinus(tier, front, pos.getZ()); z2 <= getZPlus(tier, front, pos.getZ()); z2++) {
                        Block block = world.getBlockState(new BlockPos(x2, pos.getY(), z2)).getBlock();
                        if (world.isAirBlock(pos.up())) {
                            if ((block == Blocks.GRASS || block == Blocks.DIRT)) {
                                if (!canHoe(player, stack, world, pos)) continue;
                                doParticles(stack, player, world, new BlockPos(x2, pos.getY(), z2));
                                if (!world.isRemote) {
                                    world.setBlockState(new BlockPos(x2, pos.getY(), z2), Blocks.FARMLAND.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void doParticles(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        displayParticle(world, pos, EnumParticleTypes.BLOCK_CRACK, Blocks.DIRT.getDefaultState());
        playSound(world, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS);
        ToolHelper.performTask(player, stack, getExhaustionRate(stack));
    }
}