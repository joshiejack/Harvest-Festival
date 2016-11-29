package joshie.harvest.mining.item;

import joshie.harvest.buildings.BuildingHelper;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.block.BlockLadder.Ladder;
import joshie.harvest.mining.item.ItemMiningTool.MiningTool;
import joshie.harvest.mining.tile.TileElevator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static joshie.harvest.mining.MiningHelper.*;

public class ItemMiningTool extends ItemHFEnum<ItemMiningTool, MiningTool> {
    public enum MiningTool implements IStringSerializable {
        ESCAPE_ROPE(64), ELEVATOR_CABLE(42), ELEVATOR_JUNK(1), ELEVATOR_COPPER(1), ELEVATOR_SILVER(1), ELEVATOR_GOLD(1), ELEVATOR_MYSTRIL(1);

        private int maxSize;

        MiningTool(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getStackLimit() {
            return maxSize;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemMiningTool() {
        super(HFTab.MINING, MiningTool.class);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getEnumFromStack(stack).getStackLimit();
    }

    private MiningTool getPlacerRequiredForFloor(int floor) {
        if (floor < COPPER_FLOOR) return MiningTool.ELEVATOR_JUNK;
        else if (floor < SILVER_FLOOR) return MiningTool.ELEVATOR_COPPER;
        else if (floor < GOLD_FLOOR) return MiningTool.ELEVATOR_SILVER;
        else if (floor < MYSTRIL_FLOOR) return MiningTool.ELEVATOR_GOLD;
        else return MiningTool.ELEVATOR_MYSTRIL;
    }

    private boolean isMineWall(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == HFMining.STONE;
    }

    private boolean isMineFloor(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == HFMining.DIRT;
    }

    private boolean hasMineFloorBelow(World world, BlockPos pos) {
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            if (isMineFloor(world, pos.offset(facing).down())) return true;
        }

        return false;
    }

    private boolean isElevator(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == HFMining.LADDER && HFMining.LADDER.getEnumFromState(state) == Ladder.ELEVATOR;
    }

    private void link(World world, BlockPos pos, ItemStack stack) {
        NBTTagCompound link = stack.getSubCompound("LinkData", true);
        if (!link.hasKey("Link1")) {
            link.setLong("Link1", pos.toLong());
            //Message that you set the first link coordinates
            ChatHelper.displayChat("Started link on ");
        } else {
            BlockPos link1 = BlockPos.fromLong(link.getLong("Link1"));
            if (!link1.equals(pos)) {
                int id1 = MiningHelper.getMineID(link1);
                int id2 = MiningHelper.getMineID(pos);
                if (id1 == id2) {
                    int floor1 = MiningHelper.getFloor(link1);
                    int floor2 = MiningHelper.getFloor(pos);
                    if (floor1 != floor2) {
                        int difference = floor1 >= floor2 ? floor1 - floor2 : floor2 - floor1;
                        if (stack.stackSize - difference >= 0) {
                            //Message that you have successfully linked the elevators
                            ChatHelper.displayChat("Successfully linked elevator on floor " + floor1 + " with elevator on floor " + floor2);
                            TileElevator elevator1 = (TileElevator) world.getTileEntity(link1);
                            TileElevator elevator2 = (TileElevator) world.getTileEntity(pos);
                            elevator1.setTwin(pos);
                            elevator2.setTwin(link1);
                            stack.splitStack(difference);
                            //Remove the link
                            link.removeTag("Link1");
                        } else if (difference > 42) {
                            //Message that you can have a maximum of 64 floors difference
                            ChatHelper.displayChat("Elevators cannot travel further than 42 floors");
                        } else {
                            //Message that you need a total of DIFFERENCE ropes to perform the link
                            ChatHelper.displayChat("You need " + difference + " cables to link these elevators");
                        }
                    } else ChatHelper.displayChat("Elevators cannot be linked to the same floor");
                } else {
                    //Message that elevators can't transfer between mines
                    ChatHelper.displayChat("Elevators will only work in the same mine");
                }

            } // Error that you can't link identical items
            else {
                ChatHelper.displayChat("Elevators cannot be linked to themselves");
            }
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (world.provider.getDimension() == HFMining.MINING_ID) {
            MiningTool tool = getEnumFromStack(stack);
            if (tool == MiningTool.ELEVATOR_CABLE) {
                RayTraceResult raytrace = BuildingHelper.rayTrace(player, 3.5D, 0F);
                if (raytrace == null || (raytrace.sideHit == EnumFacing.DOWN || raytrace.sideHit == EnumFacing.UP)) return new ActionResult<>(EnumActionResult.PASS, stack); //We didn't ind what we want
                BlockPos pos = raytrace.getBlockPos();
                if (isElevator(world, pos)) {
                    link(world, pos, stack);
                }

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else if (tool == MiningTool.ESCAPE_ROPE) {
                    stack.splitStack(1);
                    if (!world.isRemote)
                        MiningHelper.teleportToOverworld(player); //Back we go!
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else {
                RayTraceResult raytrace = BuildingHelper.rayTrace(player, 3.5D, 0F);
                if (raytrace == null || (raytrace.sideHit == EnumFacing.DOWN || raytrace.sideHit == EnumFacing.UP)) return new ActionResult<>(EnumActionResult.PASS, stack); //We didn't ind what we want
                BlockPos pos = raytrace.getBlockPos();
                if (isMineWall(world, pos) && isMineWall(world, pos.up()) && hasMineFloorBelow(world, pos)) {
                    int floor = MiningHelper.getFloor(pos);
                    MiningTool required = getPlacerRequiredForFloor(floor);
                    if (tool.ordinal() >= required.ordinal()) {
                        world.setBlockState(pos, HFMining.LADDER.getStateFromEnum(Ladder.ELEVATOR));
                        world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
                        stack.splitStack(1); //Reduce the stack size
                    } else ChatHelper.displayChat("Need the mining tool " + required);
                }
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.LAST;
    }
}
