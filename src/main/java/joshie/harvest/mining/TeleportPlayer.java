package joshie.harvest.mining;

import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.mining.render.ElevatorRender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TeleportPlayer {
    public static final Set<EntityPlayer> TELEPORTING = new HashSet<>();
    private static final HashMap<EntityPlayer, TeleportPlayer> TELEPORTS = new HashMap<>();
    private final EntityPlayer player;
    private final World world;
    private final BlockPos origin;
    private final BlockPos pos;
    private final EnumFacing facing;
    private long lastUpdate;
    private int timer;

    public TeleportPlayer(BlockPos origin, EntityPlayer player, EnumFacing facing, BlockPos pos, int floors) {
        this.player = player;
        this.world = player.worldObj;
        this.pos = pos;
        this.timer = Math.min(600, Math.max(60, 15 * floors));
        this.facing = facing;
        this.origin = origin;
        if (this.world.isRemote) ElevatorRender.ELEVATOR = true;
        else TELEPORTING.add(player);
    }

    public static void initiate(EntityPlayer player, BlockPos twin, EnumFacing facing, BlockPos location) {
        if (player.worldObj.isRemote) ElevatorRender.ELEVATOR = true;
        else TELEPORTING.add(player);

        //For removing when no longer colliding
        TeleportPlayer ticker = new TeleportPlayer(twin, player, facing, location, MiningHelper.getDifference(twin, location));
        MinecraftForge.EVENT_BUS.register(ticker);
        TELEPORTS.put(player, ticker);
    }

    public static void onCollide(EntityPlayer player) {
        TeleportPlayer teleporter = TELEPORTS.get(player);
        if (teleporter != null) {
            teleporter.onCollisionTick();
        }
    }

    //Clear up everything
    private void finish() {
        MinecraftForge.EVENT_BUS.unregister(this);
        if (world.isRemote) ElevatorRender.ELEVATOR = false;
        else TELEPORTS.remove(player);
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (event.world.provider.getDimension() == HFMining.MINING_ID) {
            if (event.world.getTotalWorldTime() - lastUpdate > 1) {
                finish();
            }
        }
    }

    private void onCollisionTick() {
        if (world.getTotalWorldTime() == lastUpdate) return;
        lastUpdate = world.getTotalWorldTime();
        if (world.isRemote && timer %20 == 0)
            ChatHelper.displayChat("Please wait for " + timer / 20 + " seconds...");

        timer--;
        if (timer <= 0) {
            finish(); //Unregister everything
            MiningHelper.teleportToCoordinates(player, pos);
            player.rotationYaw = facing.getHorizontalAngle();
            if (world.isRemote) {
                ElevatorRender.ELEVATOR = false;
                ChatHelper.displayChat("You have reached your destination!");
            } else TELEPORTING.remove(player);
        }
    }

    public static boolean isTeleporting(EntityPlayer player) {
        return player.worldObj.isRemote ? ElevatorRender.ELEVATOR : TELEPORTING.contains(player);
    }
}
