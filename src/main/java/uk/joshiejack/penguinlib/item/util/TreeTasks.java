package uk.joshiejack.penguinlib.item.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class TreeTasks {
    //Borrowed from Tinkers Construct by boni
    public static boolean findTree(World world, BlockPos origin) {
        BlockPos pos = null;
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(origin);
        while(!candidates.isEmpty()) {
            BlockPos candidate = candidates.pop();
            if((pos == null || candidate.getY() > pos.getY()) && world.getBlockState(candidate).getBlock().isWood(world, candidate)) {
                pos = candidate.up();
                while(world.getBlockState(pos).getBlock().isWood(world, pos)) {
                    pos = pos.up();
                }

                candidates.add(pos.north());
                candidates.add(pos.east());
                candidates.add(pos.south());
                candidates.add(pos.west());
            }
        }

        if(pos == null) return false;
        int d = 5;
        int o = -1; // -(d-1)/2
        int leaves = 0;
        for(int x = 0; x < d; x++) {
            for(int y = 0; y < d; y++) {
                for(int z = 0; z < d; z++) {
                    BlockPos leaf = pos.add(o + x, o + y, o + z);
                    IBlockState state = world.getBlockState(leaf);
                    if (state.getBlock().isLeaves(state, world, leaf)) {
                        if (++leaves >= 5) return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean replaceTree(BlockPos pos, EntityPlayer player) {
        if(player.world.isRemote) return true;
        MinecraftForge.EVENT_BUS.register(new TreeReplace(player.world, pos, player.world.getBlockState(pos)));
        return true;
    }

    static class TreeReplace {
        private final World world;
        private final IBlockState state;
        private final BlockPos pos;
        private int timer;

        TreeReplace(World world, BlockPos pos, IBlockState state) {
            this.world = world;
            this.pos = pos;
            this.state = state;
            this.timer = 0;
        }

        @SubscribeEvent
        public void replaceTrunk(TickEvent.WorldTickEvent event) {
            if(event.world.provider.getDimension() != world.provider.getDimension()) return;
            timer++;
            if(timer > 3) {
                world.setBlockState(pos, state);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

    //Borrowed from Tinkers Construct by boni
    @SuppressWarnings("WeakerAccess")
    public static class ChopTree {
        private final World world;
        private final EntityPlayer player;
        private final ItemStack stack;
        private final Queue<BlockPos> blocks = Lists.newLinkedList();
        private final Set<BlockPos> visited = new HashSet<>();
        private final NonNullList<ItemStack> drops = NonNullList.create();
        private final BlockPos start;

        public ChopTree(BlockPos start, EntityPlayer player, ItemStack stack) {
            this.world = player.getEntityWorld();
            this.player = player;
            this.stack = stack;
            this.start = start;
            this.blocks.add(start);
        }

        private void finishChoppingTree() {
            drops.forEach(i -> Block.spawnAsEntity(world, start, i));
            MinecraftForge.EVENT_BUS.unregister(this);
        }

        @SubscribeEvent
        public void chopTree(TickEvent.WorldTickEvent event) {
            if(event.world.provider.getDimension() != world.provider.getDimension()) return;
            int remaining = 4;
            while(remaining > 0) {
                if (blocks.isEmpty()) {
                    finishChoppingTree(); //Finish the dropping
                    return;
                }

                BlockPos pos = blocks.remove();
                if(!visited.add(pos)) continue; //If we've visited the block skip it
                IBlockState state = world.getBlockState(pos);
                if (!state.getBlock().isWood(world, pos)) continue; //If this block isn't wood, skip it

                //Add surrounding blocks
                for(EnumFacing facing : new EnumFacing[]{ EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST }) {
                    BlockPos pos2 = pos.offset(facing);
                    if(!visited.contains(pos2)) blocks.add(pos2);
                }

                //Add layer above
                for(int x = 0; x < 3; x++) {
                    for(int z = 0; z < 3; z++) {
                        BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
                        if(!visited.contains(pos2))  blocks.add(pos2);
                    }
                }

                //Break the extra blocks
                TerrainHelper.collectDrops(world, pos, state, player, drops, EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0);
                if (stack.getItemDamage() < stack.getMaxDamage()) {
                    stack.damageItem(1, player);
                }

                world.setBlockToAir(pos); //No particles
                remaining--;
            }
        }
    }
}
