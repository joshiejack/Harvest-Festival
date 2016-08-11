package joshie.harvest.tools.items;

import com.google.common.collect.Sets;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.ItemBaseTool;
import joshie.harvest.core.helpers.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.Stack;

import static joshie.harvest.api.gathering.ISmashable.ToolType.AXE;

public class ItemAxe extends ItemBaseTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

    public ItemAxe() {
        super("axe", EFFECTIVE_ON);
        setCreativeTab(HFTab.GATHERING);
    }


    public static class ChopTree {
        private final Stack<BlockPos> stack;
        private final World world;
        private final IBlockState state;
        private final EntityPlayer player;

        public ChopTree(World world, IBlockState state, Stack<BlockPos> stack, EntityPlayer player) {
            this.world = world;
            this.state = state;
            this.player = player;
            this.stack = new Stack<>();
            while (!stack.empty()) {
                this.stack.push(stack.pop());
            }
        }

        @SubscribeEvent
        public void chop(TickEvent.WorldTickEvent event) {
            if (event.phase != Phase.END || event.world != world) return;
            if (event.world.isRemote || stack == null || stack.size() <= 0 || stack.isEmpty()) {
                MinecraftForge.EVENT_BUS.unregister(this);
            } else {
                boolean wood = false;
                while (!wood && !stack.isEmpty()) {
                    BlockPos pos = stack.pop();
                    if (world.getBlockState(pos).getBlock() == state.getBlock()) {
                        wood = breakWood(world, pos, world.getBlockState(pos), player);
                    }
                }
            }
        }

        private boolean breakWood(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
            if (world.getBlockState(pos).getBlock() == state.getBlock()) {
                if (world.isAirBlock(pos)) return true;
                Block block = state.getBlock();
                if (!world.isRemote) {
                    int cancel = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, pos);
                    if (cancel == -1) {
                        return true;
                    }

                    if (block.removedByPlayer(state, world, pos, player, true)) {
                        block.onBlockDestroyedByPlayer(world, pos, state);
                        block.harvestBlock(world, player, pos, state, null, null);
                        block.dropXpOnBlockBreak(world, pos, cancel);
                    }

                    // always send block update to client
                    EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
                    mpPlayer.connection.sendPacket(new SPacketBlockChange(world, pos));
                }

                return true;
            } else return false;
        }
    }

    private boolean isBreakable(World world, BlockPos pos, IBlockState state) {
        return world.getBlockState(pos).getBlock() == state.getBlock() || world.getBlockState(pos).getMaterial() == Material.LEAVES;
    }

    private void add(Stack<BlockPos> queue, World world, BlockPos pos, IBlockState state) {
        if (isBreakable(world, pos, state)) {
            if (!queue.contains(pos)) {
                queue.push(pos);
            }
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (getTier(stack) == ToolTier.MYTHIC && entityLiving instanceof EntityPlayer && state.getMaterial() == Material.WOOD) {
            Stack<BlockPos> queue = new Stack<>();
            queue.add(pos); //Add the starting block

            for (int j = 0; j < 16; j++) {
                Stack<BlockPos> temp = (Stack<BlockPos>) queue.clone();
                for (BlockPos coord : temp) {
                    for (EnumFacing facing : EnumFacing.VALUES) {
                        if (facing == EnumFacing.DOWN) continue; //Skip down
                        add(queue, worldIn, coord.offset(facing), state);
                        for (EnumFacing offset: EnumFacing.VALUES) {
                            if (offset == EnumFacing.DOWN) continue; //Skip down
                            add(queue, worldIn, coord.offset(facing).offset(offset), state);
                        }
                    }
                }
            }

            MinecraftForge.EVENT_BUS.register(new ChopTree(worldIn, state, queue, (EntityPlayer) entityLiving));
            return true;
        } else return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.getEffiency(stack);
    }

    @Override
    public void onFinishedCharging(World world, EntityLivingBase entity, @Nullable RayTraceResult result, ItemStack stack, ToolTier tier) {
        if (result != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            IBlockState state = world.getBlockState(result.getBlockPos());
            if (state.getBlock() instanceof ISmashable) {
                ISmashable smashable = ((ISmashable)state.getBlock());
                if (smashable.getToolType() == AXE) {
                    if(smashable.smashBlock(player, world, result.getBlockPos(), state, tier)) {
                        ToolHelper.performTask(player, stack, getExhaustionRate(stack));
                        onBlockDestroyed(stack, world, state, result.getBlockPos(), entity);
                    }
                }
            }
        }
    }
}