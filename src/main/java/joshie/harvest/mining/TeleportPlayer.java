package joshie.harvest.mining;

import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.mining.tile.TileElevator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import javax.annotation.Nullable;
import java.util.HashMap;

public class TeleportPlayer {
    private static HashMap<EntityPlayer, TeleportTicker> SERVER_TICKER = new HashMap<>();
    private static TeleportTicker CLIENT_TICKER;

    public static boolean isTeleportTargetSetTo(EntityPlayer player, BlockPos target) {
        if (!player.worldObj.isRemote) {
            TeleportTicker teleport = SERVER_TICKER.get(player);
            return teleport != null && teleport.location.equals(target);
        } else return CLIENT_TICKER != null && CLIENT_TICKER.location.equals(target);
    }

    public static void setTeleportTargetTo(EntityPlayer player, BlockPos twin, TileEntity target, BlockPos origin) {
        TeleportTicker ticker = new TeleportTicker(player, twin, target, origin);
        if (!player.worldObj.isRemote) {
            SERVER_TICKER.put(player, ticker);
        } else CLIENT_TICKER = ticker;


        MinecraftForge.EVENT_BUS.register(ticker);
    }

    private static void clearTeleportTarget(EntityPlayer player) {
        if (!player.worldObj.isRemote) SERVER_TICKER.remove(player);
        else CLIENT_TICKER = null;
    }

    private static class TeleportTicker {
        private final EntityPlayer player;
        private final BlockPos location;
        private final TileElevator target;
        private final BlockPos origin;
        private long worldTime;
        private int counter;

        TeleportTicker(EntityPlayer player, BlockPos twin, @Nullable TileEntity target, BlockPos origin) {
            this.player = player;
            this.location = twin;
            this.target = target instanceof TileElevator ? (TileElevator) target : null;
            this.origin = origin;
            int floors = MiningHelper.getDifference(twin, origin);
            this.counter = Math.min(600, Math.max(60, 15 * floors));
        }

        private void finishOrCancelTeleport() {
            MinecraftForge.EVENT_BUS.unregister(this);
            clearTeleportTarget(player);
            if (counter <= 30 && player.worldObj.isRemote) {
                ChatHelper.displayChat(TextHelper.translate("elevator.done"));
            }
        }

        @SubscribeEvent
        public void onWorldTick(WorldTickEvent event) {
            if (isCollidingWithOrigin()) {
                if (worldTime != 0 && player.worldObj.getTotalWorldTime() == worldTime) return;
                else worldTime = player.worldObj.getTotalWorldTime();

                counter--;
                if (counter > 0 && player.worldObj.isRemote && counter %20 == 0) {
                    ChatHelper.displayChat(TextHelper.formatHF("elevator.wait", (int) Math.floor(counter / 20)));
                }

                if (counter <= 0) {
                    finishOrCancelTeleport();
                    if (!player.worldObj.isRemote) {
                        if (target != null) {
                            EnumFacing facing = target.getFacing();
                            float yaw = facing == EnumFacing.WEST ? 90F : facing == EnumFacing.EAST ? -90F : facing == EnumFacing.NORTH ? 180F : 0F;
                            MiningHelper.teleportToCoordinates(player, location.offset(facing), yaw);
                        }
                    }
                }
            } else finishOrCancelTeleport();
        }

        private boolean isCollidingWithOrigin() {
            AxisAlignedBB axisalignedbb = player.getEntityBoundingBox();
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
            BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();
            if (player.worldObj.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1)) {
                for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); ++i) {
                    for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); ++j) {
                        for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); ++k) {
                            blockpos$pooledmutableblockpos2.setPos(i, j, k);

                            if (blockpos$pooledmutableblockpos2.equals(origin) || blockpos$pooledmutableblockpos2.equals(origin.up())) {
                                return true;
                            }
                        }
                    }
                }
            }

            blockpos$pooledmutableblockpos.release();
            blockpos$pooledmutableblockpos1.release();
            blockpos$pooledmutableblockpos2.release();
            return false;
        }
    }
}
