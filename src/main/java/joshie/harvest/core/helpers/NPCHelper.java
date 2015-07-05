package joshie.harvest.core.helpers;

import java.util.UUID;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import joshie.harvest.npc.entity.EntityNPCMiner;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class NPCHelper {
    public static WorldLocation getHomeForEntity(EntityNPC entity) {
        UUID owner_uuid = entity.owning_player;
        INPC npc = entity.getNPC();
        if (npc.getHomeGroup() == null || npc.getHomeLocation() == null) return null;
        return HFTrackers.getPlayerTracker(owner_uuid).getTown().getCoordinatesFor(npc.getHomeGroup(), npc.getHomeLocation());
    }
    
    public static EntityNPC getEntityForNPC(UUID owning_player, World world, INPC npc) {
        if (npc.isBuilder()) {
            return new EntityNPCBuilder(owning_player, world, npc);
        } else if (npc.isMiner()) {
            return new EntityNPCMiner(owning_player, world, npc);
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
    
    public static int getGuiIDForNPC(INPC npc, World world, EntityPlayer player, boolean isSneaking) {
        if (isShopOpen(npc, world, player)) {
            return GuiHandler.SHOP_WELCOME;
        }
        
        return player.isSneaking() ? GuiHandler.GIFT : GuiHandler.NPC;
        //return npc.getShop() != null && npc.getShop().isOpen(world, player) && npc.getShop().getContents(player).size() > 0 ? (npc.isBuilder()? GuiHandler.SHOP_BUILDER: GuiHandler.SHOP) : (isSneaking) ? GuiHandler.GIFT : GuiHandler.NPC;
    }
}
