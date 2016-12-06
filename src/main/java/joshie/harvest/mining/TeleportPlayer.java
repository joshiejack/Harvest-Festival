package joshie.harvest.mining;

import joshie.harvest.core.helpers.ChatHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

public class TeleportPlayer {
    public static final HashMap<EntityPlayer, TeleportPlayer> TELEPORTS = new HashMap<>();
    public static TeleportPlayer TELEPORTING_CLIENT;
    private final EntityPlayer player;
    private final World world;
    private final BlockPos origin;
    private final BlockPos pos;
    private final EnumFacing facing;
    private long startTime;
    private long targetTime;

    public TeleportPlayer(BlockPos origin, EntityPlayer player, EnumFacing facing, BlockPos pos, int floors) {
        this.player = player;
        this.world = player.worldObj;
        this.pos = pos;
        this.startTime = player.worldObj.getTotalWorldTime();
        this.targetTime = startTime + Math.min(600, Math.max(60, 15 * floors));
        this.facing = facing;
        this.origin = origin;
    }

    public static void initiate(EntityPlayer player, BlockPos twin, EnumFacing facing, BlockPos location) {
        //For removing when no longer colliding
        TeleportPlayer ticker = new TeleportPlayer(twin, player, facing, location, MiningHelper.getDifference(twin, location));
        if (player.worldObj.isRemote) {
            TELEPORTING_CLIENT = ticker;
            MinecraftForge.EVENT_BUS.register(ticker);
        } else TELEPORTS.put(player, ticker);
    }

    public static void onCollide(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            if (TELEPORTING_CLIENT != null) TELEPORTING_CLIENT.onCollisionTick();
        } else {
            TeleportPlayer teleporter = TELEPORTS.get(player);
            if (teleporter != null) {
                teleporter.onCollisionTick();
            }
        }
    }

    //Clear up everything
    private void finish() {
        if (!world.isRemote) TELEPORTS.remove(player);
        else TELEPORTING_CLIENT = null;
    }

    private void onCollisionTick() {
        if (world.isRemote) {
            long timer = targetTime - world.getTotalWorldTime();
            if (timer % 20 == 0 || world.getTotalWorldTime() - 1 == startTime)  ChatHelper.displayChat("Please wait for " + (int)Math.ceil((double)timer / 20D) + " seconds...");
        }

        if (world.getTotalWorldTime() >= targetTime) {
            finish();
            float yaw = facing == EnumFacing.WEST ? 90F : facing == EnumFacing.EAST ? -90F: facing == EnumFacing.NORTH ? 180F : -180F;
            MiningHelper.teleportToCoordinates(player, pos, yaw);
            if (world.isRemote) {
                ChatHelper.displayChat("You have reached your destination!");
            }
        }
    }

    public static boolean isTeleporting(EntityPlayer player, BlockPos target) {
        if (!player.worldObj.isRemote) {
            TeleportPlayer teleport = TELEPORTS.get(player);
            return teleport != null && teleport.pos.equals(target);
        } else return TELEPORTING_CLIENT != null && TELEPORTING_CLIENT.pos.equals(target);
    }
}
