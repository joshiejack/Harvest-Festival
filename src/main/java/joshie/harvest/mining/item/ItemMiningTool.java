package joshie.harvest.mining.item;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.buildings.BuildingHelper;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.item.ItemMiningTool.MiningTool;
import joshie.harvest.mining.tile.TileElevator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class ItemMiningTool extends ItemHFEnum<ItemMiningTool, MiningTool> {
    private final Cache<EntityPlayer, NBTTagCompound> linkDataServer = CacheBuilder.newBuilder().build();
    private final Cache<EntityPlayer, NBTTagCompound> linkDataClient = CacheBuilder.newBuilder().build();
    public enum MiningTool implements IStringSerializable {
        ESCAPE_ROPE, ELEVATOR_CABLE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemMiningTool() {
        super(HFTab.MINING, MiningTool.class);
    }

    @Nullable
    private NBTTagCompound getLinkData(EntityPlayer player) {
        try {
            return player.world.isRemote ? linkDataClient.get(player, NBTTagCompound::new) : linkDataServer.get(player, NBTTagCompound::new);
        } catch (ExecutionException ex) { return null; }
    }

    private boolean isElevator(World world, BlockPos pos) {
        return world.getTileEntity(pos) instanceof TileElevator;
    }

    private int getCostBasedOnFloor(int floor) {
        return 1 + (int) Math.floor(floor / 42);
    }

    private int getCost(BlockPos link1, BlockPos pos) {
        int floor1 = MiningHelper.getFloor(link1);
        int floor2 = MiningHelper.getFloor(pos);
        int highest = floor1 >= floor2 ? floor1 : floor2;
        int lowest = floor1 < floor2 ? floor1 : floor2;

        int cost = 0;
        for (int floor = lowest; floor < highest; floor++) {
            cost += getCostBasedOnFloor(floor);
        }

        return cost;
    }

    @SuppressWarnings("ConstantConditions")
    private void link(EntityPlayer player, World world, BlockPos pos, @Nonnull ItemStack stack) {
        NBTTagCompound link = getLinkData(player);
        if (!link.hasKey("Link1")) {
            link.setLong("Link1", pos.toLong());
            //Message that you set the first link coordinates
            if (world.isRemote) ChatHelper.displayChat(TextHelper.formatHF("elevator.start", MiningHelper.getFloor(pos)));
        } else {
            BlockPos link1 = BlockPos.fromLong(link.getLong("Link1"));
            if (!link1.equals(pos)) {
                int id1 = MiningHelper.getMineID(link1);
                int id2 = MiningHelper.getMineID(pos);
                if (id1 == id2) {
                    int floor1 = MiningHelper.getFloor(link1);
                    int floor2 = MiningHelper.getFloor(pos);
                    if (floor1 != floor2) {
                        int cost = getCost(link1, pos);
                        int amount = InventoryHelper.getCount(player, stack, ITEM_STACK);
                        if (amount - cost >= 0) {
                            //Message that you have successfully linked the elevators
                            if (world.isRemote) ChatHelper.displayChat(TextHelper.formatHF("elevator.success", floor1, floor2));
                            ((TileElevator) world.getTileEntity(pos)).setTwin(link1);
                            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, stack, cost);
                            //Remove the link
                            link.removeTag("Link1");
                        } else {
                            //Message that you need a total of DIFFERENCE ropes to perform the link
                            if (world.isRemote) ChatHelper.displayChat(TextHelper.formatHF("elevator.cost", cost));
                        }
                    } else if (world.isRemote)  ChatHelper.displayChat(TextHelper.translate("elevator.cannot.floor"));
                } else if (world.isRemote)  {
                    //Message that elevators can't transfer between mines
                    ChatHelper.displayChat(TextHelper.translate("elevator.cannot.mine"));
                }

            } // Error that you can't link identical items
            else if (world.isRemote) ChatHelper.displayChat(TextHelper.translate("elevator.cannot.self"));
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.provider.getDimension() == HFMining.MINING_ID) {
            MiningTool tool = getEnumFromStack(stack);
            if (tool == MiningTool.ELEVATOR_CABLE) {
                if (player.isSneaking()) {
                    NBTTagCompound tag = getLinkData(player);
                    if (tag != null) tag.removeTag("Link1");
                } else {
                    RayTraceResult raytrace = BuildingHelper.rayTrace(player, 5D, 0F);
                    if (raytrace == null || (raytrace.sideHit == EnumFacing.DOWN || raytrace.sideHit == EnumFacing.UP))
                        return new ActionResult<>(EnumActionResult.PASS, stack); //We didn't ind what we want
                    BlockPos pos = raytrace.getBlockPos();
                    if (isElevator(world, pos.down())) link(player, world, pos.down(), stack);
                    else if (isElevator(world, pos)) link(player, world, pos, stack);
                }

                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else if (tool == MiningTool.ESCAPE_ROPE) {
                    stack.shrink(1);
                    if (!world.isRemote) {
                        MiningHelper.teleportToOverworld(player); //Back we go!
                    }
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.LAST;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        if (getEnumFromStack(stack) == MiningTool.ELEVATOR_CABLE) tooltip.add(TextFormatting.AQUA + TextHelper.translate("elevator.tooltip"));
    }
}
