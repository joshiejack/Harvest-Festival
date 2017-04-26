package joshie.harvest.tools.item;

import com.google.common.collect.Lists;
import gnu.trove.set.hash.THashSet;
import joshie.harvest.tools.HFTools;
import joshie.harvest.tools.ToolHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import javax.annotation.Nonnull;
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
    @SuppressWarnings("WeakerAccess")
    public static class ChopTree {
        private final World world;
        private final EntityPlayer player;
        @Nonnull
        private final ItemStack stack;
        private final Queue<BlockPos> blocks = Lists.newLinkedList();
        private final Set<BlockPos> visited = new THashSet<>();
        private final NonNullList<ItemStack> drops = NonNullList.create();

        ChopTree(BlockPos start, EntityPlayer player, ItemStack stack) {
            this.world = player.getEntityWorld();
            this.player = player;
            this.stack = stack;
            this.blocks.add(start);
        }

        private void finishChoppingTree() {
            BlockPos target = new BlockPos(player);
            drops.stream().forEach(i -> spawnAsEntity(world, target, i));
            MinecraftForge.EVENT_BUS.unregister(this);
        }

        @SubscribeEvent
        public void chopTree(WorldTickEvent event) {
            if(event.world.provider.getDimension() != world.provider.getDimension()) return;
            int remaining = 4;
            while(remaining > 0) {
                if (blocks.isEmpty() || !HFTools.AXE.canUse(stack)) {
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
                ToolHelper.performTask(player, stack, HFTools.AXE);
                ToolHelper.collectDrops(world, pos, state, player, drops);
                world.setBlockToAir(pos); //No particles
                remaining--;
            }
        }
    }
}
