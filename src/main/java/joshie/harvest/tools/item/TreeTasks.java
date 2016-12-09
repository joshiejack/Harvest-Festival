package joshie.harvest.tools.item;

import com.google.common.collect.Lists;
import gnu.trove.set.hash.THashSet;
import joshie.harvest.tools.HFTools;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static net.minecraft.block.Block.spawnAsEntity;

class TreeTasks {
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
        public void replaceTrunk(WorldTickEvent event) {
            if(event.world.provider.getDimension() != world.provider.getDimension()) return;
            timer++;
            if(timer > 3) {
                world.setBlockState(pos, state);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

    //Borrowed from Tinkers Construct by boni
    static class ChopTree {
        private final World world;
        private final EntityPlayer player;
        private final ItemStack stack;
        private final Queue<BlockPos> blocks = Lists.newLinkedList();
        private final Set<BlockPos> visited = new THashSet<>();

        ChopTree(BlockPos start, EntityPlayer player, ItemStack stack) {
            this.world = player.getEntityWorld();
            this.player = player;
            this.stack = stack;
            this.blocks.add(start);
        }

        @SubscribeEvent
        public void chopTree(WorldTickEvent event) {
            if(event.world.provider.getDimension() != world.provider.getDimension()) return;
            BlockPos pos;
            int remaining = 4;
            List<ItemStack> drops = new ArrayList<>();
            while(remaining > 0) {
                if(blocks.isEmpty()) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                    return;
                }

                pos = blocks.remove();
                if(!visited.add(pos)) continue;
                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (!block.isWood(world, pos)) continue;
                for(EnumFacing facing : new EnumFacing[]{ EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST }) {
                    BlockPos pos2 = pos.offset(facing);
                    if(!visited.contains(pos2)) {
                        blocks.add(pos2);
                    }
                }

                for(int x = 0; x < 3; x++) {
                    for(int z = 0; z < 3; z++) {
                        BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
                        if(!visited.contains(pos2)) {
                            blocks.add(pos2);
                        }
                    }
                }

                if (HFTools.AXE.canUse(stack)) {
                    ToolHelper.performTask(player, stack, HFTools.AXE);
                    ToolHelper.collectDrops(world, pos, state, player, drops);
                    world.setBlockToAir(pos); //No particles
                    remaining--;
                } else MinecraftForge.EVENT_BUS.unregister(this);
            }

            //Target
            BlockPos target = new BlockPos(player);
            for (ItemStack item : drops) {
                spawnAsEntity(world, target, item);
            }
        }
    }
}
