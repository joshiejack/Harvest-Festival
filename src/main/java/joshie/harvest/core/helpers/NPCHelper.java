package joshie.harvest.core.helpers;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class NPCHelper {
    public static BlockPos getHomeForEntity(EntityNPC entity) {
        UUID owner_uuid = entity.owning_player;
        INPC npc = entity.getNPC();
        if (npc.getHomeGroup() == null || npc.getHomeLocation() == null) return null;
        return HFTrackers.getPlayerTracker(owner_uuid).getTown().getCoordinatesFor(npc.getHomeGroup(), npc.getHomeLocation());
    }

    public static BlockPos getHomeForEntity(UUID owner, INPC npc) {
        if (npc.getHomeGroup() == null || npc.getHomeLocation() == null) return null;
        return HFTrackers.getPlayerTracker(owner).getTown().getCoordinatesFor(npc.getHomeGroup(), npc.getHomeLocation());
    }

    public static EntityNPC getEntityForNPC(UUID owning_player, World world, INPC npc) {
        if (npc.isBuilder()) {
            return new EntityNPCBuilder(owning_player, world, npc);
        /*} else if (npc.isMiner()) {
            return new EntityNPCMiner(owning_player, world, npc);*/
        } else if (npc.getShop() != null) {
            return new EntityNPCShopkeeper(owning_player, world, npc);
        } else return new EntityNPC(owning_player, world, npc);
    }

    public static boolean isShopOpen(INPC npc, World world, EntityPlayer player) {
        IShop shop = npc.getShop();
        if (shop != null && shop.isOpen(world, player) && shop.getContents(player).size() > 0) {
            return true;
        }

        return false;
    }

    public static int getGuiIDForNPC(INPC npc, World world, EntityPlayer player, boolean isGifting) {
        if (isShopOpen(npc, world, player)) {
            return GuiHandler.SHOP_WELCOME;
        }

        return isGifting ? GuiHandler.GIFT : GuiHandler.NPC;
    }
}